package be.kdg.prog6.warehousing.domain;

public class Buyer {
    private final BuyerId buyerId;
    private String name;
    private Address address;

    public Buyer(final String name, final Address address) {
        this.buyerId = BuyerId.newId();
        this.name = name;
        this.address = address;
    }

    public Buyer(final BuyerId buyerId, final String name, final Address address) {
        this.buyerId = buyerId;
        this.name = name;
        this.address = address;
    }

    public BuyerId getBuyerId() {
        return buyerId;
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
