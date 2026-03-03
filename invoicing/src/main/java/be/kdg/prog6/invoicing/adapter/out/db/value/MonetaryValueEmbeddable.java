package be.kdg.prog6.invoicing.adapter.out.db.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class MonetaryValueEmbeddable {
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency; // ISO 4217 currency code (e.g., "USD").

    protected MonetaryValueEmbeddable() {
    }

    public MonetaryValueEmbeddable(final BigDecimal amount, final String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }
}
