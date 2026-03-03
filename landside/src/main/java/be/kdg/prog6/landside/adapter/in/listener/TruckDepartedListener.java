package be.kdg.prog6.landside.adapter.in.listener;

import be.kdg.prog6.landside.domain.event.TruckDepartedEvent;
import be.kdg.prog6.landside.port.in.command.FulfillAppointmentCommand;
import be.kdg.prog6.landside.port.in.usecase.FulfillAppointmentUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.LANDSIDE;

@Component
public class TruckDepartedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TruckDepartedListener.class);

    private final FulfillAppointmentUseCase fulfillAppointmentUseCase;

    public TruckDepartedListener(final FulfillAppointmentUseCase fulfillAppointmentUseCase) {
        this.fulfillAppointmentUseCase = fulfillAppointmentUseCase;
    }

    /**
     * Handles TruckDepartedEvent synchronously when published.
     * This listener runs immediately, within the same transaction context as the publisher.
     * Should only be used if fulfilling the appointment before transaction commit is acceptable.
     */
    @EventListener
    public void onTruckDeparted(final TruckDepartedEvent event) {
        LOGGER.info(
            "Received {} at {} for a departed truck",
            event.getClass().getSimpleName(),
            LANDSIDE
        );
        final FulfillAppointmentCommand command = new FulfillAppointmentCommand(
            event.appointmentId()
        );
        fulfillAppointmentUseCase.fulfillAppointment(command);
    }
}
