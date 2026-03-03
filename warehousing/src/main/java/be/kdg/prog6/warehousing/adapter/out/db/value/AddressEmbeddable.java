package be.kdg.prog6.warehousing.adapter.out.db.value;

import jakarta.persistence.Embeddable;

@Embeddable
public class AddressEmbeddable {
    private String streetName;
    private String streetNumber;
    private String city;
    private String country;

    public AddressEmbeddable() {
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
