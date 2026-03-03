package be.kdg.prog6.waterside.adapter.out.publisher;

import be.kdg.prog6.common.event.waterside.ShipDepartedEvent;
import be.kdg.prog6.waterside.adapter.config.WatersideMessagingTopology;
import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.out.ShipDepartedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShipDepartedPublisher implements ShipDepartedPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShipDepartedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public ShipDepartedPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void shipDeparted(final ShippingOrder shippingOrder) {
        final String routingKey = WatersideMessagingTopology.SHIP_DEPARTED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            WatersideMessagingTopology.WATERSIDE_EVENTS_EXCHANGE,
            routingKey,
            new ShipDepartedEvent(
                shippingOrder.getShippingOrderId().id(),
                shippingOrder.getReferenceId().id()
            )
        );
    }
}
