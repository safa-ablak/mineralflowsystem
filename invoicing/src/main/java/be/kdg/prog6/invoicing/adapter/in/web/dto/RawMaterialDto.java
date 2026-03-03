package be.kdg.prog6.invoicing.adapter.in.web.dto;

import be.kdg.prog6.invoicing.domain.RawMaterial;

public record RawMaterialDto(
    String id,
    String name,
    MoneyDto storagePricePerTonPerDay,
    MoneyDto unitPricePerTon
) {
    public static RawMaterialDto fromDomain(final RawMaterial rawMaterial) {
        return new RawMaterialDto(
            rawMaterial.getId().id().toString(),
            rawMaterial.getName(),
            MoneyDto.of(rawMaterial.getStoragePricePerTonPerDay()),
            MoneyDto.of(rawMaterial.getUnitPricePerTon())
        );
    }
}
