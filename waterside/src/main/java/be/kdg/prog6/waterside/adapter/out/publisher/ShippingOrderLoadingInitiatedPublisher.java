package be.kdg.prog6.waterside.adapter.out.publisher;

import be.kdg.prog6.common.event.waterside.ShippingOrderLoadingInitiatedEvent;
import be.kdg.prog6.waterside.adapter.config.WatersideMessagingTopology;
import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.out.ShippingOrderLoadingInitiatedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShippingOrderLoadingInitiatedPublisher implements ShippingOrderLoadingInitiatedPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingOrderLoadingInitiatedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public ShippingOrderLoadingInitiatedPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void shippingOrderLoadingInitiated(final ShippingOrder shippingOrder) {
        final String routingKey = WatersideMessagingTopology.SHIPPING_ORDER_LOADING_INITIATED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            WatersideMessagingTopology.WATERSIDE_EVENTS_EXCHANGE,
            routingKey,
            new ShippingOrderLoadingInitiatedEvent(
                shippingOrder.getShippingOrderId().id(),
                shippingOrder.getReferenceId().id()
            )
        );
    }
}
