package be.kdg.prog6.waterside.domain;

import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Value Object representing a vessel number in the Waterside context.
 * Examples: "VES-00123", "SHIP-778", "IMO9381295"
 */
public record VesselNumber(String value) {
    private static final Pattern VESSEL_NO_PATTERN = Pattern.compile("^[A-Z0-9\\-]{3,15}$");

    public VesselNumber {
        requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException("Vessel number cannot be blank.");
        }
        if (!VESSEL_NO_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                "Invalid vessel number format. Must be 3-15 uppercase letters, digits, or hyphens. Got: '%s'"
                    .formatted(value)
            );
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
