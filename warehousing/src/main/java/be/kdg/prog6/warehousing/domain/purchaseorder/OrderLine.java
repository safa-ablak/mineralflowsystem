package be.kdg.prog6.warehousing.domain.purchaseorder;

import be.kdg.prog6.warehousing.domain.storage.RawMaterial;

import java.math.BigDecimal;

public class OrderLine {
    private final OrderLineId id;
    private int lineNumber;
    private final RawMaterial rawMaterial;
    private final BigDecimal amount; // unit of measure: in tons (maybe add a field called uom)

    public OrderLine(final RawMaterial rawMaterial, final BigDecimal amount) {
        this.id = OrderLineId.newId();
        // Line number is not set here, because it is set by the PurchaseOrder
        this.rawMaterial = rawMaterial;
        this.amount = amount;
    }

    public OrderLine(final OrderLineId id, final int lineNumber, final RawMaterial rawMaterial, final BigDecimal amount) {
        this.id = id;
        this.lineNumber = lineNumber;
        this.rawMaterial = rawMaterial;
        this.amount = amount;
    }

    public OrderLineId getId() {
        return id;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    // To be set by the PO the Order Line belongs to
    void setLineNumber(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final OrderLine that = (OrderLine) other;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "OrderLine{orderLineId=%s, lineNumber=%d, rawMaterial=%s, amount=%s}".formatted(
            id.id(), lineNumber, rawMaterial, amount
        );
    }
}
