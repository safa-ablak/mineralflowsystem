package be.kdg.prog6.invoicing.port.out;

import be.kdg.prog6.invoicing.domain.RawMaterial;
import be.kdg.prog6.invoicing.domain.RawMaterialId;

import java.util.List;
import java.util.Optional;

public interface LoadRawMaterialPort {
    Optional<RawMaterial> loadRawMaterialByName(String name);

    Optional<RawMaterial> loadRawMaterialById(RawMaterialId id);

    List<RawMaterial> loadRawMaterials();
}
