package be.kdg.prog6.warehousing.domain.storage;

import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Value Object.
 * Represents a strongly typed warehouse number (e.g., WH-01, WH-42).
 * Validates the format on creation.
 */
public record WarehouseNumber(String value) {
    private static final Pattern WH_NO_PATTERN = Pattern.compile("^WH-\\d{2}$"); // e.g., WH-01, WH-10

    public WarehouseNumber {
        requireNonNull(value, "Warehouse number cannot be null");
        if (!WH_NO_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                "Invalid warehouse number format: '%s'. Expected format: WH-XX (e.g., WH-01)"
                    .formatted(value)
            );
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
