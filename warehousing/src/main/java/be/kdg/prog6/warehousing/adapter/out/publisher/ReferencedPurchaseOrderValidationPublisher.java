package be.kdg.prog6.warehousing.adapter.out.publisher;

import be.kdg.prog6.common.event.warehousing.validation.ReferencedPurchaseOrderRejectedEvent;
import be.kdg.prog6.common.event.warehousing.validation.ReferencedPurchaseOrderValidatedEvent;
import be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology;
import be.kdg.prog6.warehousing.port.out.ReferencedPurchaseOrderValidationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReferencedPurchaseOrderValidationPublisher implements ReferencedPurchaseOrderValidationPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferencedPurchaseOrderValidationPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public ReferencedPurchaseOrderValidationPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void referencedPurchaseOrderValidated(final ReferencedPurchaseOrderValidatedEvent event) {
        final String routingKey = WarehousingMessagingTopology.REFERENCED_PURCHASE_ORDER_VALIDATED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            WarehousingMessagingTopology.WAREHOUSING_EVENTS_EXCHANGE,
            routingKey,
            event
        );
    }

    @Override
    public void referencedPurchaseOrderRejected(final ReferencedPurchaseOrderRejectedEvent event) {
        final String routingKey = WarehousingMessagingTopology.REFERENCED_PURCHASE_ORDER_REJECTED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            WarehousingMessagingTopology.WAREHOUSING_EVENTS_EXCHANGE,
            routingKey,
            event
        );
    }
}
