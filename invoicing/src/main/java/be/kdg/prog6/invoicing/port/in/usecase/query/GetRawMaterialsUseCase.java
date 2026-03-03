package be.kdg.prog6.invoicing.port.in.usecase.query;

import be.kdg.prog6.invoicing.domain.RawMaterial;

import java.util.List;

@FunctionalInterface
public interface GetRawMaterialsUseCase {
    List<RawMaterial> getRawMaterials();
}
