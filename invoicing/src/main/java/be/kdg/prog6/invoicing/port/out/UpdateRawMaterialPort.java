package be.kdg.prog6.invoicing.port.out;

import be.kdg.prog6.invoicing.domain.RawMaterial;

@FunctionalInterface
public interface UpdateRawMaterialPort {
    void updateRawMaterial(RawMaterial rawMaterial);
}
