package be.kdg.prog6.warehousing.adapter.out.publisher;

import be.kdg.prog6.common.event.warehousing.ShipmentRecordedEvent;
import be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology;
import be.kdg.prog6.warehousing.domain.storage.ShipmentRecord;
import be.kdg.prog6.warehousing.domain.storage.StockLevel;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.port.out.ShipmentRecordedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class WarehouseShipmentRecordedPublisher implements ShipmentRecordedPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseShipmentRecordedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public WarehouseShipmentRecordedPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // For shipments
    @Override
    public void shipmentRecorded(final Warehouse warehouse, final ShipmentRecord shipmentRecord) {
        final StockLevel stockLevel = warehouse.calculateStockLevel();
        final String routingKey =
            WarehousingMessagingTopology.WAREHOUSE_SHIPMENT_RECORDED_ROUTING_KEY_TEMPLATE.formatted(warehouse.getWarehouseId().id());
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            WarehousingMessagingTopology.WAREHOUSING_EVENTS_EXCHANGE,
            routingKey,
            new ShipmentRecordedEvent(
                warehouse.getWarehouseId().id(),
                warehouse.getSellerId().id(),
                warehouse.getRawMaterial().name(),
                shipmentRecord.shipment().amount(),
                stockLevel.percentageFilled()
            )
        );
    }
}
