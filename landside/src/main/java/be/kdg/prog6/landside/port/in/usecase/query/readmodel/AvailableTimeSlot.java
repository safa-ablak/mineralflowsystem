package be.kdg.prog6.landside.port.in.usecase.query.readmodel;

import be.kdg.prog6.landside.domain.TimeSlot;

import java.time.LocalDateTime;

/// To be used in the frontend app to display available time slots
// TODO: Renaming the `Available` prefix to `Bookable` could be better here
public record AvailableTimeSlot(
    LocalDateTime startTime,
    LocalDateTime endTime,
    int availableCapacity
) {
    public static AvailableTimeSlot fromDomain(final TimeSlot slot) {
        return new AvailableTimeSlot(
            slot.getStartTime(),
            slot.getEndTime(),
            slot.calculateAvailableCapacity()
        );
    }
}