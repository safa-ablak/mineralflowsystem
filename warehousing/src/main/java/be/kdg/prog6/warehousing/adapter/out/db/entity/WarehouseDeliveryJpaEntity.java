package be.kdg.prog6.warehousing.adapter.out.db.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(catalog = "warehousing", name = "warehouse_deliveries")
public class WarehouseDeliveryJpaEntity {
    @EmbeddedId
    private WarehouseDeliveryJpaId id;

    /* No @ManyToOne to WarehouseJpaEntity – the composite ID's warehouseId
    * is sufficient for querying, no need for a navigable relationship
    */

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "amount")
    private BigDecimal amount;

    public WarehouseDeliveryJpaId getId() {
        return id;
    }

    public void setId(final WarehouseDeliveryJpaId id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(final LocalDateTime time) {
        this.time = time;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal deliveredAmount) {
        this.amount = deliveredAmount;
    }
}
