package be.kdg.prog6.invoicing.domain;

import be.kdg.prog6.common.exception.InvalidOperationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Value object representing a monetary amount in a specific currency.
 * <p>
 * By default, amounts are expressed in USD. Precision defines how many decimal places
 * the currency uses (e.g. USD -> 2 decimals, JPY -> 0). Amounts are rounded to this
 * precision using HALF_UP rounding to ensure financial correctness.
 * <p>
 * Example:
 * <pre>{@code
 *     final Money usd = new Money(new BigDecimal("12.3456"), Currency.getInstance("USD"));
 *     System.out.println(usd.getAmount()); // prints: 12.35
 *
 *     final Money jpy = new Money(new BigDecimal("1234.567"), Currency.getInstance("JPY"));
 *     System.out.println(jpy.getAmount()); // prints: 1235
 * }</pre>
 */
public final class Money {
    // Default currency for all Money instances, unit of measure is in USD.
    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("USD");

    private final BigDecimal amount;
    private final Currency currency;

    public Money(final BigDecimal amount, final Currency currency) {
        this.amount = validateAndNormalize(amount, currency);
        this.currency = currency != null ? currency : DEFAULT_CURRENCY;
    }

    private static BigDecimal validateAndNormalize(final BigDecimal amount, final Currency currency) {
        requireNonNull(amount, "Amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative.");
        }
        final int precision = currency.getDefaultFractionDigits();
        return amount.setScale(precision, RoundingMode.HALF_UP);
    }

    // Factory methods for instantiation
    public static Money of(final BigDecimal amount) {
        return new Money(amount, DEFAULT_CURRENCY);
    }

    public static Money of(final double amount) {
        return of(BigDecimal.valueOf(amount));
    }

    public static Money ofNullable(final BigDecimal amount) {
        return amount != null ? Money.of(amount) : null;
    }

    /// Won't be used at all since we work with USD
    public static Money of(final BigDecimal amount, final Currency currency) {
        return new Money(amount, currency);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    // Arithmetic operations
    public Money add(final Money other) {
        ensureSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(final Money other) {
        ensureSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public Money multiply(final BigDecimal multiplier) {
        final BigDecimal result = this.amount.multiply(multiplier);
        return new Money(result, this.currency);
    }

    public Money divide(final BigDecimal divisor) {
        final int precision = this.currency.getDefaultFractionDigits();
        final BigDecimal result = this.amount.divide(divisor, precision, RoundingMode.HALF_UP);
        return new Money(result, this.currency);
    }

    private void ensureSameCurrency(final Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new InvalidOperationException("Currency mismatch: %s vs %s"
                .formatted(this.currency.getCurrencyCode(), other.currency.getCurrencyCode())
            );
        }
    }

    @Override
    public String toString() {
        return "%s%s".formatted(currency.getSymbol(), amount);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final Money that = (Money) other;
        return amount.equals(that.amount) && currency.equals(that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
