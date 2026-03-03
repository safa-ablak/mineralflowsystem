package be.kdg.prog6.landside.adapter.out.publisher;

import be.kdg.prog6.landside.domain.event.TruckDepartedEvent;
import be.kdg.prog6.landside.port.out.TruckDepartedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.LANDSIDE;

@Component
public class TruckDepartedPublisher implements TruckDepartedPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(TruckDepartedPublisher.class);

    private final ApplicationEventPublisher publisher;

    public TruckDepartedPublisher(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Publishes a TruckDepartedEvent as a synchronous in-process Spring application event.
     * This does NOT publish to an external message broker.<br>
     * The event is intended for use within the Landside bounded context (e.g., to fulfill the appointment).
     */
    @Override
    public void truckDeparted(final TruckDepartedEvent event) {
        LOGGER.info("Publishing {} within {}", event.getClass().getSimpleName(), LANDSIDE);
        publisher.publishEvent(event);
    }
}
