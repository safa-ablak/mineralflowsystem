package be.kdg.prog6.landside.domain;

import be.kdg.prog6.landside.domain.exception.InvalidRawMaterialException;

public enum RawMaterial {
    GYPSUM,
    IRON_ORE,
    CEMENT,
    PETCOKE,
    SLAG;

    /**
     * Validates if a given value name exists in the enum.
     *
     * @param value The value name as a string.
     * @return The corresponding RawMaterial if valid.
     * @throws InvalidRawMaterialException If the value is not recognized.
     */
    public static RawMaterial fromString(final String value) {
        for (RawMaterial rawMaterial : values()) {
            if (rawMaterial.name().equalsIgnoreCase(value)) {
                return rawMaterial;
            }
        }
        throw new InvalidRawMaterialException("Invalid Raw Material type provided: " + value);
    }
}