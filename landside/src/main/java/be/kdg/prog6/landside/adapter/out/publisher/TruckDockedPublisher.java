package be.kdg.prog6.landside.adapter.out.publisher;

import be.kdg.prog6.common.event.landside.TruckDockedEvent;
import be.kdg.prog6.landside.adapter.config.LandsideMessagingTopology;
import be.kdg.prog6.landside.port.out.TruckDockedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TruckDockedPublisher implements TruckDockedPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(TruckDockedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public TruckDockedPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void truckDocked(final TruckDockedEvent event) {
        final String routingKey = LandsideMessagingTopology.TRUCK_DOCKED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            LandsideMessagingTopology.LANDSIDE_EVENTS_EXCHANGE,
            routingKey,
            event
        );
    }
}
