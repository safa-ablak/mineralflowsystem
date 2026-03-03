package be.kdg.prog6.landside.port.in.usecase.query;

import be.kdg.prog6.landside.port.in.usecase.query.readmodel.AvailableTimeSlot;

import java.time.LocalDate;
import java.util.List;

@FunctionalInterface
public interface GetAvailableTimeSlotsUseCase {
    List<AvailableTimeSlot> getAvailableTimeSlotsFor(LocalDate date);
}
