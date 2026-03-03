package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.common.event.warehousing.PurchaseOrderShippedEvent;
import be.kdg.prog6.warehousing.adapter.out.publisher.PurchaseOrderShippedPublisher;
import be.kdg.prog6.warehousing.domain.exception.purchaseorder.PurchaseOrderNotFoundException;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;
import be.kdg.prog6.warehousing.domain.service.ShipmentService;
import be.kdg.prog6.warehousing.domain.storage.ShipmentRecord;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.port.in.command.ShipPurchaseOrderCommand;
import be.kdg.prog6.warehousing.port.in.usecase.ShipPurchaseOrderUseCase;
import be.kdg.prog6.warehousing.port.out.LoadPurchaseOrderPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import be.kdg.prog6.warehousing.port.out.ShipmentRecordedPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShipPurchaseOrderUseCaseImpl implements ShipPurchaseOrderUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShipPurchaseOrderUseCaseImpl.class);

    private final LoadPurchaseOrderPort loadPurchaseOrderPort;
    private final LoadWarehousePort loadWarehousePort;
    private final ShipmentService shipmentService;
    private final List<ShipmentRecordedPort> shipmentRecordedPorts;
    private final PurchaseOrderShippedPublisher purchaseOrderShippedPublisher; // publish after all shipments are recorded

    public ShipPurchaseOrderUseCaseImpl(final LoadPurchaseOrderPort loadPurchaseOrderPort,
                                        final LoadWarehousePort loadWarehousePort,
                                        final List<ShipmentRecordedPort> shipmentRecordedPorts,
                                        final PurchaseOrderShippedPublisher purchaseOrderShippedPublisher) {
        this.loadPurchaseOrderPort = loadPurchaseOrderPort;
        this.loadWarehousePort = loadWarehousePort;
        this.shipmentService = new ShipmentService(); // Instantiating the domain service
        this.shipmentRecordedPorts = shipmentRecordedPorts;
        this.purchaseOrderShippedPublisher = purchaseOrderShippedPublisher;
    }

    // Records shipments for a Purchase Order from the relevant warehouse(s)
    @Override
    @Transactional
    public void shipPurchaseOrder(final ShipPurchaseOrderCommand command) {
        final PurchaseOrderId purchaseOrderId = command.purchaseOrderId();
        LOGGER.info("Shipping Purchase Order with ID {}", purchaseOrderId.id());
        // 1. Load the purchase order from the db
        final PurchaseOrder purchaseOrder = loadPurchaseOrderPort.loadPurchaseOrderById(purchaseOrderId).orElseThrow(
            () -> PurchaseOrderNotFoundException.forId(purchaseOrderId)
        );
        // 2. Load the warehouses of the seller from the db -> This is the right way to do it.
        final List<Warehouse> sellerWarehouses =
            loadWarehousePort.loadWarehousesBySellerIdForShipment(purchaseOrder.getSellerId());
        // 3. For the purchase order ship from warehouses via the domain service
        final Map<Warehouse, ShipmentRecord> warehouseToShipmentRecord = shipmentService.shipForPurchaseOrderFrom(
            purchaseOrder,
            sellerWarehouses
        );
        // 4. Save to mutated aggregate(s) to the db + publish the shipment event per warehouse.
        warehouseToShipmentRecord.forEach((warehouse, shipmentRecord) ->
            shipmentRecordedPorts.forEach(
                port -> port.shipmentRecorded(warehouse, shipmentRecord)
            )
        );
        // 5. Publish the shipment completion event after all the shipments are recorded for the PO
        purchaseOrderShippedPublisher.purchaseOrderShipped(new PurchaseOrderShippedEvent(purchaseOrderId.id()));
        final int numberOfAffectedWarehouses = warehouseToShipmentRecord.size();
        LOGGER.info(
            "Purchase Order with ID {} has been shipped, {} Warehouse(s) in total affected",
            purchaseOrderId, numberOfAffectedWarehouses
        );
    }
}
