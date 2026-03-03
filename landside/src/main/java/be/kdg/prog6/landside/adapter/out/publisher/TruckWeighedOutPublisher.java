package be.kdg.prog6.landside.adapter.out.publisher;

import be.kdg.prog6.common.event.landside.TruckWeighedOutEvent;
import be.kdg.prog6.landside.adapter.config.LandsideMessagingTopology;
import be.kdg.prog6.landside.port.out.TruckWeighedOutPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TruckWeighedOutPublisher implements TruckWeighedOutPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(TruckWeighedOutPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public TruckWeighedOutPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void truckWeighedOut(final TruckWeighedOutEvent event) {
        final String routingKey = LandsideMessagingTopology.TRUCK_WEIGHED_OUT_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        rabbitTemplate.convertAndSend(
            LandsideMessagingTopology.LANDSIDE_EVENTS_EXCHANGE,
            routingKey,
            event
        );
    }
}
