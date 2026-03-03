package be.kdg.prog6.warehousing.adapter.out.db.entity;

import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(catalog = "warehousing", name = "order_lines")
public class OrderLineJpaEntity {
    @Id
    @Column(name = "order_line_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID orderLineId;

    @Column(name = "line_number")
    private int lineNumber;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrderJpaEntity purchaseOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "raw_material", nullable = false)
    private RawMaterial rawMaterial;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    public UUID getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(final UUID orderLineId) {
        this.orderLineId = orderLineId;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public PurchaseOrderJpaEntity getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(final PurchaseOrderJpaEntity purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(final RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }
}
