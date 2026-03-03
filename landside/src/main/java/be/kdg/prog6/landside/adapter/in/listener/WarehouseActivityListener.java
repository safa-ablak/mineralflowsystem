package be.kdg.prog6.landside.adapter.in.listener;

import be.kdg.prog6.common.event.warehousing.DeliveryRecordedEvent;
import be.kdg.prog6.common.event.warehousing.ShipmentRecordedEvent;
import be.kdg.prog6.landside.domain.WarehouseId;
import be.kdg.prog6.landside.port.in.command.ProjectAvailabilityCommand;
import be.kdg.prog6.landside.port.in.projector.WarehouseAvailabilityProjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.landside.adapter.config.LandsideMessagingTopology.RECORDED_WAREHOUSE_DELIVERIES_QUEUE;
import static be.kdg.prog6.landside.adapter.config.LandsideMessagingTopology.RECORDED_WAREHOUSE_SHIPMENTS_QUEUE;

@Component
public class WarehouseActivityListener { // Listens for warehouse activities (deliveries and shipments)
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseActivityListener.class);

    private final WarehouseAvailabilityProjector warehouseAvailabilityProjector;

    public WarehouseActivityListener(final WarehouseAvailabilityProjector warehouseAvailabilityProjector) {
        this.warehouseAvailabilityProjector = warehouseAvailabilityProjector;
    }

    @RabbitListener(queues = RECORDED_WAREHOUSE_DELIVERIES_QUEUE)
    public void onDeliveryRecorded(final DeliveryRecordedEvent event) {
        LOGGER.info(
            "Delivery recorded at Warehouse {} of Supplier {} storing Raw Material {} with Amount of {}",
            event.warehouseId(),
            event.sellerId(), // Context Mapping: Seller is our Supplier
            event.rawMaterial(),
            event.amountDelivered()
        );
        final ProjectAvailabilityCommand command = new ProjectAvailabilityCommand(
            WarehouseId.of(event.warehouseId()),
            event.percentageFilled()
        );
        warehouseAvailabilityProjector.projectAvailability(command);
    }

    @RabbitListener(queues = RECORDED_WAREHOUSE_SHIPMENTS_QUEUE)
    public void onShipmentRecorded(final ShipmentRecordedEvent event) {
        LOGGER.info(
            "Shipment recorded at Warehouse {} of Supplier {} storing Raw Material {} with Amount of {}",
            event.warehouseId(),
            event.sellerId(), // Context Mapping: Seller is our Supplier
            event.rawMaterial(),
            event.amountShipped()
        );
        final ProjectAvailabilityCommand command = new ProjectAvailabilityCommand(
            WarehouseId.of(event.warehouseId()),
            event.percentageFilled()
        );
        warehouseAvailabilityProjector.projectAvailability(command);
    }
}