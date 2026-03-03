package be.kdg.prog6.warehousing.domain.purchaseorder;

import static java.util.Objects.requireNonNull;

// Value object: PurchaseOrderNumber
public record PurchaseOrderNumber(String value) {
    public static final String PO_NUMBER_FORMAT = "PO\\d{6}";

    public PurchaseOrderNumber {
        requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException("Purchase Order Number cannot be empty");
        }
        if (!value.matches(PO_NUMBER_FORMAT)) {
            throw new IllegalArgumentException("Invalid Purchase Order Number format. Example of a valid format: 'PO123456'");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}