package be.kdg.prog6.warehousing.adapter.out.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(catalog = "warehousing", name = "warehouse_shipment_allocations")
public class WarehouseShipmentAllocationJpaEntity {
    @EmbeddedId
    private WarehouseShipmentAllocationJpaId id;

    @Column(name = "amount_allocated", nullable = false)
    private BigDecimal amountAllocated;

    public WarehouseShipmentAllocationJpaEntity() {}

    public WarehouseShipmentAllocationJpaId getId() {
        return id;
    }

    public void setId(final WarehouseShipmentAllocationJpaId id) {
        this.id = id;
    }

    public BigDecimal getAmountAllocated() {
        return amountAllocated;
    }

    public void setAmountAllocated(final BigDecimal amountAllocated) {
        this.amountAllocated = amountAllocated;
    }
}