package be.kdg.prog6.landside.port.in.usecase;

import be.kdg.prog6.landside.domain.Appointment;
import be.kdg.prog6.landside.port.in.command.MakeAppointmentCommand;

@FunctionalInterface
public interface MakeAppointmentUseCase {
    Appointment makeAppointment(MakeAppointmentCommand command);
}
