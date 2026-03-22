package be.kdg.prog6.warehousing.adapter.out.publisher;

import be.kdg.prog6.common.event.warehousing.WarehouseRawMaterialAssignedEvent;
import be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.port.out.RawMaterialAssignedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class WarehouseRawMaterialAssignedPublisher implements RawMaterialAssignedPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseRawMaterialAssignedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public WarehouseRawMaterialAssignedPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void rawMaterialAssigned(final Warehouse warehouse) {
        final String routingKey = WarehousingMessagingTopology.WAREHOUSE_RAW_MATERIAL_ASSIGNED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            WarehousingMessagingTopology.WAREHOUSING_EVENTS_EXCHANGE,
            routingKey,
            new WarehouseRawMaterialAssignedEvent(
                warehouse.getWarehouseId().id(),
                warehouse.getRawMaterial().name()
            )
        );
    }
}
