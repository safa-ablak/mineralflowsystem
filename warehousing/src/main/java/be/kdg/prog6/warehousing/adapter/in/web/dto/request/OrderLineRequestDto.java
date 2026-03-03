package be.kdg.prog6.warehousing.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderLineRequestDto(
    @NotNull(message = "Raw material is required")
    String rawMaterial,
    @NotNull(message = "Amount is required")
    BigDecimal amount
) {
}
