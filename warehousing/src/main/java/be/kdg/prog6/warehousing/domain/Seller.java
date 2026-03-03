package be.kdg.prog6.warehousing.domain;

public class Seller {
    private final SellerId sellerId;
    private String name;
    private Address address;

    public Seller(final String name, final Address address) {
        this.sellerId = SellerId.newId();
        this.name = name;
        this.address = address;
    }

    public Seller(final SellerId sellerId, final String name, final Address address) {
        this.sellerId = sellerId;
        this.name = name;
        this.address = address;
    }

    public SellerId getSellerId() {
        return sellerId;
    }

    public String getName() {
        return name;
    }

    private void setName(final String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    private void setAddress(final Address address) {
        this.address = address;
    }
}
