package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.RawMaterialSummary;

import java.math.BigDecimal;

public record RawMaterialSummaryDto(
    String name,
    BigDecimal totalAmount
) {
    public static RawMaterialSummaryDto of(final RawMaterialSummary rawMaterialSummary) {
        return new RawMaterialSummaryDto(
            rawMaterialSummary.rawMaterial().name(),
            rawMaterialSummary.totalAmount()
        );
    }
}
