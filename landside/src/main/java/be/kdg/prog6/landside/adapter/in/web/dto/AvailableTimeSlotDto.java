package be.kdg.prog6.landside.adapter.in.web.dto;

import be.kdg.prog6.landside.port.in.usecase.query.readmodel.AvailableTimeSlot;

import java.time.LocalDateTime;

public record AvailableTimeSlotDto(
    LocalDateTime startTime,
    LocalDateTime endTime,
    int availableCapacity
) {
    public static AvailableTimeSlotDto of(final AvailableTimeSlot slot) {
        return new AvailableTimeSlotDto(
            slot.startTime(),
            slot.endTime(),
            slot.availableCapacity()
        );
    }
}
