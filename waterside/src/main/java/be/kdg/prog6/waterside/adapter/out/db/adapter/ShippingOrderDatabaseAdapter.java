package be.kdg.prog6.waterside.adapter.out.db.adapter;

import be.kdg.prog6.waterside.adapter.out.db.entity.BunkeringOperationJpaEntity;
import be.kdg.prog6.waterside.adapter.out.db.entity.InspectionOperationJpaEntity;
import be.kdg.prog6.waterside.adapter.out.db.entity.ShippingOrderJpaEntity;
import be.kdg.prog6.waterside.adapter.out.db.repository.ShippingOrderJpaRepository;
import be.kdg.prog6.waterside.domain.*;
import be.kdg.prog6.waterside.port.out.CreateShippingOrderPort;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import be.kdg.prog6.waterside.port.out.UpdateShippingOrderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ShippingOrderDatabaseAdapter implements CreateShippingOrderPort, LoadShippingOrderPort, UpdateShippingOrderPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingOrderDatabaseAdapter.class);

    private final ShippingOrderJpaRepository shippingOrderJpaRepository;

    public ShippingOrderDatabaseAdapter(final ShippingOrderJpaRepository shippingOrderJpaRepository) {
        this.shippingOrderJpaRepository = shippingOrderJpaRepository;
    }

    @Override
    public void createShippingOrder(final ShippingOrder shippingOrder) {
        LOGGER.info("Creating Shipping Order with ID {}", shippingOrder.getShippingOrderId().id());
        final ShippingOrderJpaEntity shippingOrderEntity = toShippingOrderJpaEntity(shippingOrder);
        shippingOrderJpaRepository.save(shippingOrderEntity);
    }

    @Override
    public Optional<ShippingOrder> loadById(final ShippingOrderId id) {
        LOGGER.info("Loading Shipping Order by ID {}", id.id());
        return shippingOrderJpaRepository.findByShippingOrderId(id.id()).map(this::toShippingOrder);
    }

    @Override
    public Optional<ShippingOrder> loadByReferenceId(final ReferenceId referenceId) {
        LOGGER.info("Loading Shipping Order by Reference ID {}", referenceId.id());
        return shippingOrderJpaRepository.findByReferenceId(referenceId.id()).map(this::toShippingOrder);
    }

    @Override
    public List<ShippingOrder> loadShippingOrdersOnSite() {
        LOGGER.info("Loading Shipping Orders on site");
        return shippingOrderJpaRepository.findShippingOrdersOnSite().stream().map(this::toShippingOrder).toList();
    }

    @Override
    public int countShippingOrdersOnSite() {
        LOGGER.info("Counting Shipping Orders on site");
        return shippingOrderJpaRepository.countShippingOrdersOnSite();
    }

    @Override
    public List<ShippingOrder> loadShippingOrders() {
        LOGGER.info("Loading all Shipping Orders");
        return shippingOrderJpaRepository.findAll().stream().map(this::toShippingOrder).toList();
    }

    @Override
    public List<ShippingOrder> loadShippingOrdersWithQueuedBunkeringByOldestFirst() {
        LOGGER.info("Loading Shipping Orders with Queued Bunkering Operations");
        return shippingOrderJpaRepository.findByBunkeringOperationStatusOrderedByQueuedAt(BunkeringOperationStatus.QUEUED)
            .stream()
            .map(this::toShippingOrder)
            .toList();
    }

    private ShippingOrderJpaEntity toShippingOrderJpaEntity(final ShippingOrder shippingOrder) {
        final ShippingOrderJpaEntity shippingOrderJpa = new ShippingOrderJpaEntity();
        final UUID soId = shippingOrder.getShippingOrderId().id();
        shippingOrderJpa.setShippingOrderId(soId);
        shippingOrderJpa.setReferenceId(shippingOrder.getReferenceId().id());
        shippingOrderJpa.setBuyerId(shippingOrder.getBuyerId().id());
        shippingOrderJpa.setVesselNumber(shippingOrder.getVesselNumber().value());
        shippingOrderJpa.setScheduledArrivalDate(shippingOrder.getScheduledArrivalDate());
        shippingOrderJpa.setScheduledDepartureDate(shippingOrder.getScheduledDepartureDate());
        shippingOrderJpa.setActualArrivalDate(shippingOrder.getActualArrivalDate());
        shippingOrderJpa.setActualDepartureDate(shippingOrder.getActualDepartureDate());
        shippingOrderJpa.setStatus(shippingOrder.getStatus());
        shippingOrderJpa.setInspectionOperation(
            toJpaInspectionOperationEntity(shippingOrder.getInspection(), soId)
        );
        shippingOrderJpa.setBunkeringOperation(
            toJpaBunkeringOperationEntity(shippingOrder.getBunkering(), soId)
        );
        return shippingOrderJpa;
    }

    private InspectionOperationJpaEntity toJpaInspectionOperationEntity(final InspectionOperation inspectionOperation,
                                                                        final UUID shippingOrderId) {
        final InspectionOperationJpaEntity inspectionJpa = new InspectionOperationJpaEntity();
        inspectionJpa.setId(inspectionOperation.getId().id());
        inspectionJpa.setShippingOrderId(shippingOrderId);
        inspectionJpa.setPerformedOn(inspectionOperation.getPerformedOn());
        inspectionJpa.setInspectorSignature(inspectionOperation.getInspectorSignature());
        inspectionJpa.setStatus(inspectionOperation.getStatus());
        return inspectionJpa;
    }

    private BunkeringOperationJpaEntity toJpaBunkeringOperationEntity(final BunkeringOperation bunkeringOperation,
                                                                      final UUID shippingOrderId) {
        final BunkeringOperationJpaEntity bunkeringJpa = new BunkeringOperationJpaEntity();
        bunkeringJpa.setId(bunkeringOperation.getId().id());
        bunkeringJpa.setShippingOrderId(shippingOrderId);
        bunkeringJpa.setQueuedAt(bunkeringOperation.getQueuedAt());
        bunkeringJpa.setPerformedAt(bunkeringOperation.getPerformedAt());
        bunkeringJpa.setStatus(bunkeringOperation.getStatus());
        return bunkeringJpa;
    }

    private ShippingOrder toShippingOrder(final ShippingOrderJpaEntity shippingOrderJpa) {
        final InspectionOperation inspectionOperation = toDomainInspectionOperation(shippingOrderJpa.getInspectionOperation());
        final BunkeringOperation bunkeringOperation = toDomainBunkeringOperation(shippingOrderJpa.getBunkeringOperation());
        return new ShippingOrder(
            ShippingOrderId.of(shippingOrderJpa.getShippingOrderId()),
            ReferenceId.of(shippingOrderJpa.getReferenceId()),
            new VesselNumber(shippingOrderJpa.getVesselNumber()),
            BuyerId.of(shippingOrderJpa.getBuyerId()),
            shippingOrderJpa.getScheduledArrivalDate(),
            shippingOrderJpa.getScheduledDepartureDate(),
            shippingOrderJpa.getActualArrivalDate(),
            shippingOrderJpa.getActualDepartureDate(),
            shippingOrderJpa.getStatus(),
            inspectionOperation,
            bunkeringOperation
        );
    }

    private InspectionOperation toDomainInspectionOperation(final InspectionOperationJpaEntity inspectionJpa) {
        return new InspectionOperation(
            InspectionOperationId.of(inspectionJpa.getId()),
            inspectionJpa.getPerformedOn(),
            inspectionJpa.getInspectorSignature(),
            inspectionJpa.getStatus()
        );
    }

    private BunkeringOperation toDomainBunkeringOperation(final BunkeringOperationJpaEntity bunkeringJpa) {
        return new BunkeringOperation(
            BunkeringOperationId.of(bunkeringJpa.getId()),
            bunkeringJpa.getQueuedAt(),
            bunkeringJpa.getPerformedAt(),
            bunkeringJpa.getStatus()
        );
    }

    @Override
    public void updateShippingOrder(final ShippingOrder shippingOrder) {
        LOGGER.info("Updating Shipping Order with ID {}", shippingOrder.getShippingOrderId().id());
        final ShippingOrderJpaEntity shippingOrderJpaEntity = toShippingOrderJpaEntity(shippingOrder);
        shippingOrderJpaRepository.save(shippingOrderJpaEntity);
    }
}
