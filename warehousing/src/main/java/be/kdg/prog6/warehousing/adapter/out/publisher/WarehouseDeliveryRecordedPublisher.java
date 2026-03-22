package be.kdg.prog6.warehousing.adapter.out.publisher;

import be.kdg.prog6.common.event.warehousing.WarehouseDeliveryRecordedEvent;
import be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology;
import be.kdg.prog6.warehousing.domain.storage.Delivery;
import be.kdg.prog6.warehousing.domain.storage.StockLevel;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.port.out.DeliveryRecordedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class WarehouseDeliveryRecordedPublisher implements DeliveryRecordedPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseDeliveryRecordedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public WarehouseDeliveryRecordedPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // For deliveries
    @Override
    public void deliveryRecorded(final Warehouse warehouse, final Delivery delivery) {
        final StockLevel stockLevel = warehouse.calculateStockLevel();
        final String routingKey =
            WarehousingMessagingTopology.WAREHOUSE_DELIVERY_RECORDED_ROUTING_KEY_TEMPLATE.formatted(warehouse.getWarehouseId().id());
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            WarehousingMessagingTopology.WAREHOUSING_EVENTS_EXCHANGE,
            routingKey,
            new WarehouseDeliveryRecordedEvent(
                warehouse.getWarehouseId().id(),
                warehouse.getSellerId().id(),
                warehouse.getRawMaterial().name(),
                delivery.amount(),
                stockLevel.percentageFilled()
            )
        );
    }
}
