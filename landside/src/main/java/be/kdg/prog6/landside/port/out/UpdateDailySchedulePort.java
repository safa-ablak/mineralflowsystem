package be.kdg.prog6.landside.port.out;

import be.kdg.prog6.landside.domain.DailySchedule;

@FunctionalInterface
public interface UpdateDailySchedulePort {
    void updateDailySchedule(DailySchedule dailySchedule);
}