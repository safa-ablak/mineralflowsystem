package be.kdg.prog6.invoicing.adapter.out.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(catalog = "invoicing", name = "yearly_commission_rates")
public class YearlyCommissionRateJpaEntity {
    @Id
    private int year;
    private BigDecimal rate;

    protected YearlyCommissionRateJpaEntity() {}

    public YearlyCommissionRateJpaEntity(final int year, final BigDecimal rate) {
        this.year = year;
        this.rate = rate;
    }

    public int getYear() {
        return year;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
