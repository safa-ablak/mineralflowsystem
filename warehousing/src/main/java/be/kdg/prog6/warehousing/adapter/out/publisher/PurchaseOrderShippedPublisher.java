package be.kdg.prog6.warehousing.adapter.out.publisher;

import be.kdg.prog6.common.event.warehousing.PurchaseOrderShippedEvent;
import be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology;
import be.kdg.prog6.warehousing.port.out.PurchaseOrderShippedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderShippedPublisher implements PurchaseOrderShippedPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderShippedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public PurchaseOrderShippedPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /*
     * # Publish when the raw materials have been successfully shipped from all the warehouses involved for a Purchase Order #
     */
    @Override
    public void purchaseOrderShipped(final PurchaseOrderShippedEvent event) {
        final String routingKey = WarehousingMessagingTopology.PURCHASE_ORDER_SHIPPED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            WarehousingMessagingTopology.WAREHOUSING_EVENTS_EXCHANGE,
            routingKey,
            event
        );
    }
}
