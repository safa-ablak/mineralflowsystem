package be.kdg.prog6.invoicing;

import be.kdg.prog6.invoicing.domain.Money;
import be.kdg.prog6.invoicing.domain.RawMaterial;
import be.kdg.prog6.invoicing.domain.RawMaterialId;
import be.kdg.prog6.invoicing.port.out.LoadRawMaterialPort;

import java.util.List;
import java.util.Optional;

/**
 * Test stub for {@link LoadRawMaterialPort} that returns a fixed raw material.
 * <p>
 * - {@code loadRawMaterialByName(RAW_MATERIAL_NAME)} returns the raw material instance<br>
 * - {@code loadRawMaterialById(RAW_MATERIAL_ID)} returns the raw material instance<br>
 * - {@code loadRawMaterials()} returns a list containing only the raw material<br>
 * - All other queries return {@code Optional.empty()} or empty list
 */
class LoadRawMaterialPortStub implements LoadRawMaterialPort {
    @Override
    public Optional<RawMaterial> loadRawMaterialByName(final String name) {
        if (TestIds.RAW_MATERIAL_NAME.equalsIgnoreCase(name)) {
            return Optional.of(createRawMaterial());
        }
        return Optional.empty();
    }

    @Override
    public Optional<RawMaterial> loadRawMaterialById(final RawMaterialId id) {
        if (TestIds.RAW_MATERIAL_ID.equals(id)) {
            return Optional.of(createRawMaterial());
        }
        return Optional.empty();
    }

    @Override
    public List<RawMaterial> loadRawMaterials() {
        return List.of(createRawMaterial());
    }

    private static RawMaterial createRawMaterial() {
        return new RawMaterial(
            TestIds.RAW_MATERIAL_ID,
            TestIds.RAW_MATERIAL_NAME,
            Money.of(5),    // $5 per ton per day
            Money.of(110)   // $110 per ton unit price
        );
    }
}
