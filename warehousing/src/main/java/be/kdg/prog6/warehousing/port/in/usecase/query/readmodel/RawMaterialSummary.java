package be.kdg.prog6.warehousing.port.in.usecase.query.readmodel;

import be.kdg.prog6.warehousing.domain.storage.RawMaterial;

import java.math.BigDecimal;

// Using this read model to display a summary for the specified raw material
public record RawMaterialSummary(
    RawMaterial rawMaterial,
    BigDecimal totalAmount
) {
}
