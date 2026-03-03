package be.kdg.prog6.warehousing.adapter.out.publisher;

import be.kdg.prog6.common.event.warehousing.PayloadDeliveryTicketGeneratedEvent;
import be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology;
import be.kdg.prog6.warehousing.domain.storage.PayloadDeliveryTicket;
import be.kdg.prog6.warehousing.port.out.CreatePayloadDeliveryTicketPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PayloadDeliveryTicketGeneratedPublisher implements CreatePayloadDeliveryTicketPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayloadDeliveryTicketGeneratedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public PayloadDeliveryTicketGeneratedPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void createPayloadDeliveryTicket(final PayloadDeliveryTicket ticket) {
        final String routingKey = WarehousingMessagingTopology.WAREHOUSE_PDT_GENERATED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            WarehousingMessagingTopology.WAREHOUSING_EVENTS_EXCHANGE,
            routingKey,
            new PayloadDeliveryTicketGeneratedEvent(
                ticket.id().id(),
                ticket.warehouseNumber(),
                ticket.rawMaterial(),
                ticket.generationTime(),
                ticket.dockNumber()
            )
        );
    }
}
