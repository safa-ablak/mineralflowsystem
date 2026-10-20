package be.kdg.prog6.landside.domain.exception;

import java.time.LocalDate;

public class DailyScheduleNotAvailableException extends LandsideDomainException {
    public DailyScheduleNotAvailableException(final LocalDate date) {
        super(
            String.format("No Daily Schedule available for date %s", date)
        );
    }
}