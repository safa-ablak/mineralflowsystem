package be.kdg.prog6.warehousing.domain.storage;

import be.kdg.prog6.warehousing.domain.exception.storage.InvalidRawMaterialException;

import static java.util.Objects.requireNonNull;

/**
 * Enumeration representing different types of raw materials.
 * Each type has a human-readable name for better representation.
 */
public enum RawMaterial {
    /**
     * <b>Gypsum</b>: A soft sulfate mineral composed of calcium sulfate dihydrate.<br></br>
     * <i>Usage</i>: Commonly used in construction for producing plaster, plasterboard, and cement.
     * It is also used in agriculture as a soil conditioner and fertilizer.
     */
    GYPSUM("Gypsum"),
    /**
     * <b>Iron Ore</b>: A naturally occurring mineral from which iron is extracted.<br></br>
     * <i>Usage</i>: Crucial in the production of steel, widely used in construction, manufacturing, and transportation.
     */
    IRON_ORE("Iron Ore"),
    /**
     * <b>Cement</b>: A binder substance used in construction that sets, hardens, and adheres to other materials.<br></br>
     * <i>Usage</i>: Key ingredient in concrete, mortar, and stucco. Portland cement, made from limestone and clay, is the most common type.
     */
    CEMENT("Cement"),
    /**
     * <b>Petcoke</b>: A carbon-rich solid material derived from oil refining.<br></br>
     * <i>Usage</i>: Used as fuel in power generation, cement kilns, and other industrial processes, and in electrode production for aluminum and steel industries.
     */
    PETCOKE("Petcoke"),
    /**
     * <b>Slag</b>: A byproduct of the smelting process used to produce metals from ores.<br></br>
     * <i>Usage</i>: Used in construction as an aggregate in concrete and road construction, and as a raw material in cement production.
     */
    SLAG("Slag");

    /**
     * Human-readable name of the raw material.
     */
    private final String displayName;

    /**
     * Constructor for RawMaterial.
     *
     * @param displayName Human-readable of the raw material.
     */
    RawMaterial(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the human-readable name of the raw material.
     *
     * @return Human-readable name of the raw material.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Resolves a raw material name to a {@link RawMaterial}.
     * <p>
     * Tries to match by display name (case-insensitive) or enum name (case-insensitive with underscores).
     *
     * @param value the material name to resolve
     * @return the corresponding {@link RawMaterial}
     * @throws InvalidRawMaterialException if no match is found
     */
    public static RawMaterial fromString(final String value) {
        requireNonNull(value);
        if (value.isBlank()) {
            throw new InvalidRawMaterialException("Raw material name cannot be empty");
        }
        final String normalizedValue = value.toUpperCase().replace(' ', '_');
        for (RawMaterial rm : values()) {
            if (rm.getDisplayName().equalsIgnoreCase(value) || rm.name().equals(normalizedValue)) {
                return rm;
            }
        }
        throw new InvalidRawMaterialException("Invalid Raw Material: %s".formatted(value));
    }
}
