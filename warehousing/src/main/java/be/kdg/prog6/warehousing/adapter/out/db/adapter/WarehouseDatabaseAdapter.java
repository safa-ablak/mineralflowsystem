package be.kdg.prog6.warehousing.adapter.out.db.adapter;

import be.kdg.prog6.warehousing.adapter.out.db.entity.*;
import be.kdg.prog6.warehousing.adapter.out.db.repository.ShipmentAllocationJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.repository.WarehouseDeliveryJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.repository.WarehouseJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.repository.WarehouseShipmentJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.value.SiteLocationEmbeddable;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.storage.*;
import be.kdg.prog6.warehousing.domain.storage.Balance;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.BalanceSnapshot;
import be.kdg.prog6.warehousing.port.out.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WarehouseDatabaseAdapter
    implements LoadWarehousePort, BalanceSnapshottedPort, DeliveryRecordedPort, ShipmentRecordedPort, RawMaterialAssignedPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseDatabaseAdapter.class);

    private final WarehouseJpaRepository warehouseJpaRepository;
    private final WarehouseDeliveryJpaRepository warehouseDeliveryJpaRepository;
    private final WarehouseShipmentJpaRepository warehouseShipmentJpaRepository;
    private final ShipmentAllocationJpaRepository shipmentAllocationJpaRepository;
    // Used instead of implementing the port here, because snapshotting is a separate responsibility
    // WarehouseDatabaseAdapter uses the port to delegate snapshot persistence and loading,
    // maintaining clean separation of concerns as per DDD and Hexagonal Architecture
    private final BalanceSnapshotPort balanceSnapshotPort;

    public WarehouseDatabaseAdapter(final WarehouseJpaRepository warehouseJpaRepository,
                                    final WarehouseDeliveryJpaRepository warehouseDeliveryJpaRepository,
                                    final WarehouseShipmentJpaRepository warehouseShipmentJpaRepository,
                                    final ShipmentAllocationJpaRepository shipmentAllocationJpaRepository,
                                    final BalanceSnapshotPort balanceSnapshotPort) {
        this.warehouseJpaRepository = warehouseJpaRepository;
        this.warehouseDeliveryJpaRepository = warehouseDeliveryJpaRepository;
        this.warehouseShipmentJpaRepository = warehouseShipmentJpaRepository;
        this.shipmentAllocationJpaRepository = shipmentAllocationJpaRepository;
        this.balanceSnapshotPort = balanceSnapshotPort;
    }

    // For summary of a raw material on-site by type.
    @Override
    public List<Warehouse> loadWarehousesByRawMaterial(final RawMaterial rawMaterial) {
        LOGGER.info("Loading Warehouses by Raw Material {}", rawMaterial.getDisplayName());
        return warehouseJpaRepository.findByRawMaterial(rawMaterial).stream().map(this::toWarehouse).toList();
    }

    @Override
    public Optional<Warehouse> loadWarehouseById(final WarehouseId id) {
        return warehouseJpaRepository.findById(id.id()).map(this::toWarehouse); // jpa -> domain
    }

    // These methods are for the frontend drill-down page.
    // 1 -) It performs a hefty query: fetching all activities (deliveries and shipments) of a warehouse.
    @Override
    public Optional<Warehouse> loadWarehouseByIdWithAllActivities(final WarehouseId id) {
        LOGGER.info("Loading Warehouse by ID {} with all Activities (Deliveries and Shipments)", id.id());
        return warehouseJpaRepository.findById(id.id()).map(this::toWarehouseWithAllActivities);
    }

    // 2 -) Even heftier: fetching the shipment allocations on top of all activities (for allocation tracking = delivery <-> shipment traceability).
    @Override
    public Optional<Warehouse> loadWarehouseByIdWithAllActivitiesAndAllocations(final WarehouseId id) {
        LOGGER.info("Loading Warehouse by ID {} with all Activities and Allocations", id.id());
        return warehouseJpaRepository.findById(id.id()).map(this::toWarehouseWithAllActivitiesAndAllocations);
    }

    @Override
    public List<Warehouse> loadWarehousesBySellerId(final SellerId sellerId) {
        LOGGER.info("Loading Warehouses by Seller ID {}", sellerId.id());
        return warehouseJpaRepository.findBySellerId(sellerId.id()).stream().map(this::toWarehouse).toList();
    }

    @Override
    public List<Warehouse> loadWarehousesBySellerIdForShipment(final SellerId sellerId) {
        LOGGER.info("Loading Warehouses by Seller ID {} for shipment", sellerId.id());
        return getWarehousesBySellerIdWithAllDeliveriesAndAllocations(sellerId);
    }

    // To provide an overview of all the warehouses
    @Override
    public List<Warehouse> loadWarehouses() {
        LOGGER.info("Loading all Warehouses");
        return warehouseJpaRepository.findAll().stream().map(this::toWarehouse).toList();
    }

    // To report warehouse storage (for invoicing ctx)
    @Override
    public List<Warehouse> loadWarehousesBySellerIdForReporting(final SellerId sellerId) {
        LOGGER.info("Loading all Warehouses for reporting (with all Deliveries and Allocations)");
        return getWarehousesBySellerIdWithAllDeliveriesAndAllocations(sellerId);
    }

    private List<Warehouse> getWarehousesBySellerIdWithAllDeliveriesAndAllocations(final SellerId sellerId) {
        return warehouseJpaRepository.findBySellerId(sellerId.id())
            .stream()
            .map(this::toWarehouseWithAllDeliveriesAndAllocations)
            .toList();
    }

    // For querying Net Warehouse Balance change between two dates
    @Override
    public Optional<Warehouse> loadWarehouseByIdWithActivitiesBetween(
        final WarehouseId id,
        final LocalDateTime from, final LocalDateTime to
    ) {
        LOGGER.info("Loading Warehouse by ID {} with Activities (Deliveries and Shipments) between {} and {}",
            id.id(), from, to
        );
        return warehouseJpaRepository.findById(id.id())
            .map(warehouseJpaEntity
                -> toWarehouseWithActivitiesBetween(warehouseJpaEntity, from, to));
    }

    // Used to update balance in the "warehouses" table based on the snapshot
    // And separately save the snapshot in the "balance_snapshots" table
    @Override
    public void balanceSnapshotted(final Warehouse warehouse) {
        final UUID id = warehouse.getWarehouseId().id();
        final WarehouseJpaEntity warehouseJpaEntity = warehouseJpaRepository.findById(id).orElseThrow();
        final StockLevel stockLevel = warehouse.calculateStockLevel();
        final Balance balance = stockLevel.balance();
        warehouseJpaEntity.setBalance(balance.amount());
        warehouseJpaEntity.setBalanceUpdatedAt(balance.time());
        warehouseJpaEntity.setPercentageFilled(stockLevel.percentageFilled());
        final BalanceSnapshot balanceSnapshot = new BalanceSnapshot(
            warehouse.getWarehouseId(), balance.time(), balance.amount()
        );
        balanceSnapshotPort.saveBalanceSnapshot(balanceSnapshot);
        warehouseJpaRepository.save(warehouseJpaEntity);
    }

    @Override
    public void rawMaterialAssigned(final Warehouse warehouse) {
        final UUID id = warehouse.getWarehouseId().id();
        final WarehouseJpaEntity warehouseJpaEntity = warehouseJpaRepository.findById(id).orElseThrow();
        warehouseJpaEntity.setRawMaterial(warehouse.getRawMaterial());
        warehouseJpaRepository.save(warehouseJpaEntity);
    }

    // For deliveries
    @Override
    public void deliveryRecorded(final Warehouse warehouse, final Delivery delivery) {
        final UUID warehouseId = warehouse.getWarehouseId().id();
        final WarehouseJpaEntity warehouseJpaEntity = warehouseJpaRepository.findById(warehouseId).orElseThrow();
        final StockLevel stockLevel = warehouse.calculateStockLevel();
        warehouseJpaEntity.setBalance(stockLevel.balance().amount());
        warehouseJpaEntity.setBalanceUpdatedAt(stockLevel.balance().time());
        warehouseJpaEntity.setPercentageFilled(stockLevel.percentageFilled());
        LOGGER.info("Saving Delivery for Warehouse {} with amount of {}", warehouseId, delivery.amount());
        warehouseJpaRepository.save(warehouseJpaEntity);
        warehouseDeliveryJpaRepository.save(toWarehouseDeliveryJpaEntity(delivery));
    }

    // For shipments
    @Override
    public void shipmentRecorded(final Warehouse warehouse, final ShipmentRecord shipmentRecord) {
        final UUID warehouseId = warehouse.getWarehouseId().id();
        final WarehouseJpaEntity warehouseJpaEntity = warehouseJpaRepository.findById(warehouseId).orElseThrow();
        final StockLevel stockLevel = warehouse.calculateStockLevel();
        warehouseJpaEntity.setBalance(stockLevel.balance().amount());
        warehouseJpaEntity.setBalanceUpdatedAt(stockLevel.balance().time());
        warehouseJpaEntity.setPercentageFilled(stockLevel.percentageFilled());
        final Shipment shipment = shipmentRecord.shipment();
        LOGGER.info("Saving Shipment for Warehouse {} with amount of {}", warehouseId, shipment.amount());
        warehouseJpaRepository.save(warehouseJpaEntity);
        warehouseShipmentJpaRepository.save(toWarehouseShipmentJpaEntity(shipment));
        final List<ShipmentAllocation> allocations = shipmentRecord.allocations();
        LOGGER.info("Saving Allocations of Shipment {} for Warehouse {}", shipment.id().id(), warehouseId);
        saveShipmentAllocations(allocations);
    }

    private void saveShipmentAllocations(final List<ShipmentAllocation> allocations) {
        final List<ShipmentAllocationJpaEntity> allocationJpaEntities = allocations.stream()
            .map(WarehouseDatabaseAdapter::toShipmentAllocationJpaEntity)
            .toList();
        shipmentAllocationJpaRepository.saveAll(allocationJpaEntities);
    }

    private WarehouseDeliveryJpaEntity toWarehouseDeliveryJpaEntity(final Delivery delivery) {
        final WarehouseDeliveryJpaEntity warehouseDeliveryJpaEntity = new WarehouseDeliveryJpaEntity();
        warehouseDeliveryJpaEntity.setId(WarehouseDeliveryJpaId.of(delivery.id()));
        warehouseDeliveryJpaEntity.setTime(delivery.time());
        warehouseDeliveryJpaEntity.setAmount(delivery.amount());
        return warehouseDeliveryJpaEntity;
    }

    private WarehouseShipmentJpaEntity toWarehouseShipmentJpaEntity(final Shipment shipment) {
        final WarehouseShipmentJpaEntity warehouseShipmentJpaEntity = new WarehouseShipmentJpaEntity();
        warehouseShipmentJpaEntity.setId(WarehouseShipmentJpaId.of(shipment.id()));
        warehouseShipmentJpaEntity.setTime(shipment.time());
        warehouseShipmentJpaEntity.setAmount(shipment.amount());
        return warehouseShipmentJpaEntity;
    }

    private Warehouse toWarehouse(final WarehouseJpaEntity warehouseJpaEntity) {
        final StockLedger stockLedger = toStockLedger(warehouseJpaEntity);
        return buildWarehouse(warehouseJpaEntity, stockLedger);
    }

    private Warehouse toWarehouseWithAllDeliveriesAndAllocations(final WarehouseJpaEntity warehouseJpaEntity) {
        final StockLedger stockLedger = toStockLedgerWithAllDeliveriesAndAllocations(warehouseJpaEntity);
        return buildWarehouse(warehouseJpaEntity, stockLedger);
    }

    private Warehouse toWarehouseWithActivitiesBetween(
        final WarehouseJpaEntity warehouseJpaEntity,
        final LocalDateTime from, final LocalDateTime to
    ) {
        final StockLedger stockLedger = toStockLedgerWithActivitiesBetween(warehouseJpaEntity, from, to);
        return buildWarehouseWithOriginBalance(warehouseJpaEntity, stockLedger);
    }

    private Warehouse toWarehouseWithAllActivities(final WarehouseJpaEntity warehouseJpaEntity) {
        final StockLedger stockLedger = toStockLedgerWithAllActivities(warehouseJpaEntity);
        return buildWarehouse(warehouseJpaEntity, stockLedger);
    }

    private Warehouse toWarehouseWithAllActivitiesAndAllocations(final WarehouseJpaEntity warehouseJpaEntity) {
        final StockLedger stockLedger = toStockLedgerWithAllActivitiesAndAllocations(warehouseJpaEntity);
        return buildWarehouse(warehouseJpaEntity, stockLedger);
    }

    private Warehouse buildWarehouse(final WarehouseJpaEntity warehouseJpaEntity, final StockLedger stockLedger) {
        final WarehouseId warehouseId = WarehouseId.of(warehouseJpaEntity.getWarehouseId());
        final SellerId sellerId = SellerId.of(warehouseJpaEntity.getSellerId());
        final WarehouseNumber warehouseNumber = new WarehouseNumber(warehouseJpaEntity.getWarehouseNumber());
        final RawMaterial rawMaterial = warehouseJpaEntity.getRawMaterial();
        final Balance baseBalance = toBalance(warehouseJpaEntity);
        final SiteLocation siteLocation = toSiteLocation(warehouseJpaEntity.getSiteLocation());
        return new Warehouse(warehouseId, sellerId, warehouseNumber, rawMaterial, baseBalance, stockLedger, siteLocation);
    }

    private Warehouse buildWarehouseWithOriginBalance(final WarehouseJpaEntity warehouseJpaEntity, final StockLedger stockLedger) {
        final WarehouseId warehouseId = WarehouseId.of(warehouseJpaEntity.getWarehouseId());
        final SellerId sellerId = SellerId.of(warehouseJpaEntity.getSellerId());
        final WarehouseNumber warehouseNumber = new WarehouseNumber(warehouseJpaEntity.getWarehouseNumber());
        final RawMaterial rawMaterial = warehouseJpaEntity.getRawMaterial();
        final SiteLocation siteLocation = toSiteLocation(warehouseJpaEntity.getSiteLocation());
        return new Warehouse(warehouseId, sellerId, warehouseNumber, rawMaterial, Balance.ORIGIN, stockLedger, siteLocation);
    }

    // For recording deliveries AND querying
    private StockLedger toStockLedger(final WarehouseJpaEntity warehouseJpaEntity) {
        final UUID warehouseId = warehouseJpaEntity.getWarehouseId();
        final List<Delivery> deliveries = warehouseDeliveryJpaRepository
            .findAllById_WarehouseIdAndTimeAfter(warehouseId, warehouseJpaEntity.getBalanceUpdatedAt())
            .stream()
            .map(WarehouseDatabaseAdapter::toDelivery)
            .collect(Collectors.toList());
        final List<Shipment> shipments = warehouseShipmentJpaRepository
            .findAllById_WarehouseIdAndTimeAfter(warehouseId, warehouseJpaEntity.getBalanceUpdatedAt())
            .stream()
            .map(WarehouseDatabaseAdapter::toShipment)
            .collect(Collectors.toList());
        // For querying AND recording deliveries, allocations are not needed -> List.of()
        return new StockLedger(WarehouseId.of(warehouseId), deliveries, shipments, List.of());
    }

    // For recording shipments AND reporting storage(for invoicing ctx)
    private StockLedger toStockLedgerWithAllDeliveriesAndAllocations(final WarehouseJpaEntity warehouseJpaEntity) {
        final UUID warehouseId = warehouseJpaEntity.getWarehouseId();
        final List<Delivery> deliveries = warehouseDeliveryJpaRepository
            // We need all the deliveries, in the domain layer we will filter for the shippable ones
            .findAllById_WarehouseId(warehouseId)
            .stream()
            .map(WarehouseDatabaseAdapter::toDelivery)
            .collect(Collectors.toList());
        final List<Shipment> shipments = warehouseShipmentJpaRepository
            .findAllById_WarehouseIdAndTimeAfter(warehouseId, warehouseJpaEntity.getBalanceUpdatedAt())
            .stream()
            .map(WarehouseDatabaseAdapter::toShipment)
            .collect(Collectors.toList());
        final List<ShipmentAllocation> allocations = shipmentAllocationJpaRepository
            .findAllById_WarehouseId(warehouseId)
            .stream()
            .map(WarehouseDatabaseAdapter::toShipmentAllocation)
            .collect(Collectors.toList());
        return new StockLedger(WarehouseId.of(warehouseId), deliveries, shipments, allocations);
    }

    // With activities (deliveries and shipments) between two given dates, for querying Net Warehouse Balance Change
    private StockLedger toStockLedgerWithActivitiesBetween(
        final WarehouseJpaEntity warehouseJpaEntity,
        final LocalDateTime from, final LocalDateTime to
    ) {
        final UUID warehouseId = warehouseJpaEntity.getWarehouseId();
        final List<Delivery> deliveriesBetween = warehouseDeliveryJpaRepository
            .findAllById_WarehouseIdAndTimeBetween(warehouseId, from, to)
            .stream()
            .map(WarehouseDatabaseAdapter::toDelivery)
            .toList();
        final List<Shipment> shipmentsBetween = warehouseShipmentJpaRepository
            .findAllById_WarehouseIdAndTimeBetween(warehouseId, from, to)
            .stream()
            .map(WarehouseDatabaseAdapter::toShipment)
            .toList();
        // Allocations are not needed for querying Warehouse Balance Change -> List.of()
        return new StockLedger(WarehouseId.of(warehouseId), deliveriesBetween, shipmentsBetween, List.of());
    }

    // With all activities (all deliveries and shipments, for the frontend drill down page)
    private StockLedger toStockLedgerWithAllActivities(final WarehouseJpaEntity warehouseJpaEntity) {
        final UUID warehouseId = warehouseJpaEntity.getWarehouseId();
        final List<Delivery> deliveries = warehouseDeliveryJpaRepository
            .findAllById_WarehouseId(warehouseId)
            .stream()
            .map(WarehouseDatabaseAdapter::toDelivery)
            .toList();
        final List<Shipment> shipments = warehouseShipmentJpaRepository
            .findAllById_WarehouseId(warehouseId)
            .stream()
            .map(WarehouseDatabaseAdapter::toShipment)
            .toList();
        // Allocations are not needed for `basic` activity history -> List.of()
        return new StockLedger(WarehouseId.of(warehouseId), deliveries, shipments, List.of());
    }

    private StockLedger toStockLedgerWithAllActivitiesAndAllocations(final WarehouseJpaEntity warehouseJpaEntity) {
        final UUID warehouseId = warehouseJpaEntity.getWarehouseId();
        final List<Delivery> deliveries = warehouseDeliveryJpaRepository
            .findAllById_WarehouseId(warehouseId)
            .stream()
            .map(WarehouseDatabaseAdapter::toDelivery)
            .toList();
        final List<Shipment> shipments = warehouseShipmentJpaRepository
            .findAllById_WarehouseId(warehouseId)
            .stream()
            .map(WarehouseDatabaseAdapter::toShipment)
            .toList();
        final List<ShipmentAllocation> allocations = shipmentAllocationJpaRepository
            .findAllById_WarehouseId(warehouseId)
            .stream()
            .map(WarehouseDatabaseAdapter::toShipmentAllocation)
            .toList();
        // Load allocations for `full-fledged` activity history (for allocation tracking)
        return new StockLedger(WarehouseId.of(warehouseId), deliveries, shipments, allocations);
    }

    private static Delivery toDelivery(final WarehouseDeliveryJpaEntity deliveryJpa) {
        return new Delivery(
            DeliveryId.of(WarehouseId.of(deliveryJpa.getId().getWarehouseId()), deliveryJpa.getId().getDeliveryId()),
            deliveryJpa.getTime(),
            deliveryJpa.getAmount()
        );
    }

    private static Shipment toShipment(final WarehouseShipmentJpaEntity shipmentJpa) {
        return new Shipment(
            ShipmentId.of(WarehouseId.of(shipmentJpa.getId().getWarehouseId()), shipmentJpa.getId().getShipmentId()),
            shipmentJpa.getTime(),
            shipmentJpa.getAmount()
        );
    }

    private static Balance toBalance(final WarehouseJpaEntity warehouseJpaEntity) {
        return new Balance(warehouseJpaEntity.getBalanceUpdatedAt(), warehouseJpaEntity.getBalance());
    }

    private static SiteLocation toSiteLocation(final SiteLocationEmbeddable embeddable) {
        return new SiteLocation(embeddable.getEasting(), embeddable.getNorthing());
    }

    private static ShipmentAllocationJpaEntity toShipmentAllocationJpaEntity(final ShipmentAllocation allocation) {
        final ShipmentAllocationJpaId id = ShipmentAllocationJpaId.of(
            allocation.warehouseId(),
            allocation.shipmentId(),
            allocation.deliveryId()
        );
        final ShipmentAllocationJpaEntity allocationJpaEntity = new ShipmentAllocationJpaEntity();
        allocationJpaEntity.setId(id);
        allocationJpaEntity.setAmountAllocated(allocation.amountAllocated());
        /* No JPA relationship linking(with delivery & shipment) needed, the composite ID is sufficient for querying */
        return allocationJpaEntity;
    }

    private static ShipmentAllocation toShipmentAllocation(final ShipmentAllocationJpaEntity entity) {
        final WarehouseId warehouseId = WarehouseId.of(entity.getId().getWarehouseId());
        return new ShipmentAllocation(
            warehouseId,
            ShipmentId.of(warehouseId, entity.getId().getShipmentId()),
            DeliveryId.of(warehouseId, entity.getId().getDeliveryId()),
            entity.getAmountAllocated()
        );
    }
}