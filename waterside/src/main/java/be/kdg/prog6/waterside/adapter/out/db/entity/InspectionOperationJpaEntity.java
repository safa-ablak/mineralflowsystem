package be.kdg.prog6.waterside.adapter.out.db.entity;

import be.kdg.prog6.waterside.domain.InspectionOperationStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(catalog = "waterside", name = "inspection_operations")
public class InspectionOperationJpaEntity {
    @Id
    @Column(name = "id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "shipping_order_id", columnDefinition = "varchar(36)", unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID shippingOrderId;

    @Column(name = "performed_on")
    private LocalDate performedOn;

    @Column(name = "inspector_signature")
    private String inspectorSignature;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InspectionOperationStatus status;

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

    public LocalDate getPerformedOn() {
        return performedOn;
    }

    public void setPerformedOn(final LocalDate performedOn) {
        this.performedOn = performedOn;
    }

    public String getInspectorSignature() {
        return inspectorSignature;
    }

    public void setInspectorSignature(final String inspectorSignature) {
        this.inspectorSignature = inspectorSignature;
    }

    public InspectionOperationStatus getStatus() {
        return status;
    }

    public void setStatus(final InspectionOperationStatus status) {
        this.status = status;
    }
}
