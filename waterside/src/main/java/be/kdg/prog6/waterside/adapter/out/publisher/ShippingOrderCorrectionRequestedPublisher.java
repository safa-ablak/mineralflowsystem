package be.kdg.prog6.waterside.adapter.out.publisher;

import be.kdg.prog6.common.event.waterside.ShippingOrderCorrectionRequestedEvent;
import be.kdg.prog6.waterside.adapter.config.WatersideMessagingTopology;
import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.out.ShippingOrderCorrectionRequestedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShippingOrderCorrectionRequestedPublisher implements ShippingOrderCorrectionRequestedPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingOrderCorrectionRequestedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public ShippingOrderCorrectionRequestedPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void shippingOrderCorrectionRequested(final ShippingOrder shippingOrder) {
        final String routingKey = WatersideMessagingTopology.SHIPPING_ORDER_CORRECTION_REQUESTED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            WatersideMessagingTopology.WATERSIDE_EVENTS_EXCHANGE,
            routingKey,
            new ShippingOrderCorrectionRequestedEvent(
                shippingOrder.getBuyerId().id(),
                shippingOrder.getShippingOrderId().id(),
                shippingOrder.getReferenceId().id(),
                shippingOrder.getVesselNumber().value()
            ));
    }
}
