package be.kdg.prog6.invoicing.adapter.in.web.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateRawMaterialPricingDto(
    @Nullable @Positive BigDecimal storagePricePerTonPerDay,
    @Nullable @Positive BigDecimal unitPricePerTon
) {}
