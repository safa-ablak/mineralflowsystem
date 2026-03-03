package be.kdg.prog6.warehousing.adapter.out.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(catalog = "warehousing", name = "shipment_allocations")
public class ShipmentAllocationJpaEntity {
    @EmbeddedId
    private ShipmentAllocationJpaId id;

    @Column(name = "amount_allocated", nullable = false)
    private BigDecimal amountAllocated;

    public ShipmentAllocationJpaEntity() {
    }

    public ShipmentAllocationJpaId getId() {
        return id;
    }

    public void setId(final ShipmentAllocationJpaId id) {
        this.id = id;
    }

    public BigDecimal getAmountAllocated() {
        return amountAllocated;
    }

    public void setAmountAllocated(final BigDecimal amountAllocated) {
        this.amountAllocated = amountAllocated;
    }
}