package be.kdg.prog6.landside.port.out;

import be.kdg.prog6.landside.domain.AppointmentId;
import be.kdg.prog6.landside.domain.DailySchedule;

import java.time.LocalDate;
import java.util.Optional;

public interface LoadDailySchedulePort {
    Optional<DailySchedule> loadDailyScheduleByDate(LocalDate date);

    Optional<DailySchedule> loadDailyScheduleForAppointmentId(AppointmentId appointmentId);
}