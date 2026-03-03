package be.kdg.prog6.landside.domain;

import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

// Value Object representing a Truck License Plate
public record TruckLicensePlate(String value) {
    private static final Pattern PLATE_PATTERN = Pattern.compile("^[A-Z0-9\\-]{2,12}$");

    public TruckLicensePlate {
        requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException("Truck License Plate cannot be blank.");
        }
        final String normalizedValue = value.trim().toUpperCase();
        if (!PLATE_PATTERN.matcher(normalizedValue).matches()) {
            throw new IllegalArgumentException(
                "Invalid Truck License Plate format: '%s'. Expected alphanumeric (2-12 chars, dashes allowed)"
                    .formatted(value)
            );
        }
        value = normalizedValue;
    }

    @Override
    public String toString() {
        return value;
    }
}
