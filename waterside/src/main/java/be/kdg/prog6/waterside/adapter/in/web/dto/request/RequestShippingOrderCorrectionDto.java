package be.kdg.prog6.waterside.adapter.in.web.dto.request;

import java.util.UUID;

public record RequestShippingOrderCorrectionDto(
    UUID buyerId,
    UUID referenceId
) {}
