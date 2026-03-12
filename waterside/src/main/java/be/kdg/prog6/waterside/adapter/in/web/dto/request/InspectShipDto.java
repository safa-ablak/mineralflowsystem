package be.kdg.prog6.waterside.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;

public record InspectShipDto(
    @NotNull String inspectorSignature
) {}
