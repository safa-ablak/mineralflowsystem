package be.kdg.prog6.waterside.adapter.out.db.entity;

import be.kdg.prog6.waterside.domain.BunkeringOperationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "waterside", name = "bunkering_operations")
public class BunkeringOperationJpaEntity {
    @Id
    @Column(name = "id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "shipping_order_id", columnDefinition = "varchar(36)", unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID shippingOrderId;

    @Column(name = "queued_at")
    private LocalDateTime queuedAt;

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private BunkeringOperationStatus status;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public UUID getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(final UUID shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    public LocalDateTime getQueuedAt() {
        return queuedAt;
    }

    public void setQueuedAt(final LocalDateTime queuedAt) {
        this.queuedAt = queuedAt;
    }

    public LocalDateTime getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(final LocalDateTime performedAt) {
        this.performedAt = performedAt;
    }

    public BunkeringOperationStatus getStatus() {
        return status;
    }

    public void setStatus(final BunkeringOperationStatus status) {
        this.status = status;
    }
}
