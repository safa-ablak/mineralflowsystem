package be.kdg.prog6.landside.domain.exception;

import java.time.LocalDate;

public class DailyScheduleNotAvailableException extends LandsideDomainException {
    private DailyScheduleNotAvailableException(final String message) {
        super(message);
    }

    public static DailyScheduleNotAvailableException forDate(final LocalDate date) {
        return new DailyScheduleNotAvailableException(
            String.format("No Daily Schedule available for date %s", date)
        );
    }
}