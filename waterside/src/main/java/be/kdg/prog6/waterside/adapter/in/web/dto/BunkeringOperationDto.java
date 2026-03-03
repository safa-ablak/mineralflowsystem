package be.kdg.prog6.waterside.adapter.in.web.dto;

import be.kdg.prog6.waterside.domain.BunkeringOperation;

import java.time.LocalDateTime;

public record BunkeringOperationDto(
    LocalDateTime queuedAt,
    LocalDateTime performedAt,
    String status
) {
    public static BunkeringOperationDto fromDomain(final BunkeringOperation bunkeringOperation) {
        return new BunkeringOperationDto(
            bunkeringOperation.getQueuedAt(),
            bunkeringOperation.getPerformedAt(),
            bunkeringOperation.getStatus().name()
        );
    }
}
