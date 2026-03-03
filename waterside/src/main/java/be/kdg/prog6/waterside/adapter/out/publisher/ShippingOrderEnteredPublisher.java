package be.kdg.prog6.waterside.adapter.out.publisher;

import be.kdg.prog6.common.event.waterside.ShippingOrderEnteredEvent;
import be.kdg.prog6.waterside.adapter.config.WatersideMessagingTopology;
import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.out.CreateShippingOrderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShippingOrderEnteredPublisher implements CreateShippingOrderPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingOrderEnteredPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public ShippingOrderEnteredPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void createShippingOrder(final ShippingOrder shippingOrder) {
        final String routingKey = WatersideMessagingTopology.SHIPPING_ORDER_ENTERED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            WatersideMessagingTopology.WATERSIDE_EVENTS_EXCHANGE,
            routingKey,
            new ShippingOrderEnteredEvent(
                shippingOrder.getBuyerId().id(),
                shippingOrder.getShippingOrderId().id(),
                shippingOrder.getReferenceId().id(),
                shippingOrder.getVesselNumber().value()
            )
        );
    }
}
