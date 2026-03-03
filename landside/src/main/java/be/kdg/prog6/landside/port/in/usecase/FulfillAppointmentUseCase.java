package be.kdg.prog6.landside.port.in.usecase;

import be.kdg.prog6.landside.port.in.command.FulfillAppointmentCommand;

@FunctionalInterface
public interface FulfillAppointmentUseCase {
    void fulfillAppointment(FulfillAppointmentCommand command);
}
