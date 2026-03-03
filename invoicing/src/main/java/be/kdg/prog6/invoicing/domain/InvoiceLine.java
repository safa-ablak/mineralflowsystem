package be.kdg.prog6.invoicing.domain;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public class InvoiceLine {
    private final InvoiceLineId id;
    private final Money amount;
    private final InvoiceLineType type;

    public InvoiceLine(final Money amount, final InvoiceLineType type) {
        requireNonNull(amount, "Amount cannot be null");
        if (amount.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invoice line amount must be greater than 0");
        }
        requireNonNull(type, "Invoice line type cannot be null");
        this.id = InvoiceLineId.newId();
        this.amount = amount;
        this.type = type;
    }

    public InvoiceLine(final InvoiceLineId id, final Money amount, final InvoiceLineType type) {
        this.id = id;
        this.amount = amount;
        this.type = type;
    }

    // Factory methods
    static InvoiceLine createCommissionFee(final Money baseAmount, final BigDecimal commissionRate) {
        requireNonNull(commissionRate);
        if (commissionRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Commission rate cannot be negative!");
        }
        final Money commissionAmount = baseAmount.multiply(commissionRate);
        return new InvoiceLine(commissionAmount, InvoiceLineType.COMMISSION);
    }

    static InvoiceLine createStorageCostFee(final RawMaterial rawMaterial, final BigDecimal amount) {
        requireNonNull(rawMaterial);
        requireNonNull(amount);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        final Money storageCost = rawMaterial.getStoragePricePerTonPerDay().multiply(amount);
        return new InvoiceLine(storageCost, InvoiceLineType.STORAGE_COST);
    }

    // Getters
    public InvoiceLineId getId() {
        return id;
    }

    public Money getAmount() {
        return amount;
    }

    public InvoiceLineType getType() {
        return type;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof InvoiceLine that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "InvoiceLine{id=%s, amount=%s, type=%s}".formatted(id, amount, type);
    }
}
