package be.kdg.prog6.waterside.adapter.in.web.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record EnterShippingOrderDto(
    @NotNull UUID buyerId,
    @NotNull UUID referenceId,
    @NotNull String vesselNumber,
    @Future LocalDateTime scheduledArrivalDate,
    @Future LocalDateTime scheduledDepartureDate
) {}