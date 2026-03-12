package be.kdg.prog6.landside.adapter.in.web.dto;

import be.kdg.prog6.landside.port.in.usecase.query.readmodel.BookableTimeSlot;

import java.time.LocalDateTime;

public record BookableTimeSlotDto(
    LocalDateTime startTime,
    LocalDateTime endTime,
    int remainingSpots
) {
    public static BookableTimeSlotDto from(final BookableTimeSlot slot) {
        return new BookableTimeSlotDto(
            slot.startTime(),
            slot.endTime(),
            slot.remainingSpots()
        );
    }
}