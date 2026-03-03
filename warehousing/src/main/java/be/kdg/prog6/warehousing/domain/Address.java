package be.kdg.prog6.warehousing.domain;

import static java.util.Objects.requireNonNull;

// Value object: Address
public record Address(String streetName, String streetNumber, String city, String country) {
    public Address {
        requireNonNull(streetName, "Street name must not be null");
        requireNonNull(streetNumber, "Street number must not be null");
        requireNonNull(city, "City must not be null");
        requireNonNull(country, "Country must not be null");
        if (streetName.isBlank()) {
            throw new IllegalArgumentException("Street name must not be blank");
        }
        if (streetNumber.isBlank()) {
            throw new IllegalArgumentException("Street number must not be blank");
        }
        if (city.isBlank()) {
            throw new IllegalArgumentException("City must not be blank");
        }
        if (country.isBlank()) {
            throw new IllegalArgumentException("Country must not be blank");
        }
    }

    @Override
    public String toString() {
        return "%s %s, %s, %s".formatted(streetName, streetNumber, city, country);
    }
}
