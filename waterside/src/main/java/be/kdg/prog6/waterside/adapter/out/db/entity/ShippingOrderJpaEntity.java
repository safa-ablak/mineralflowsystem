package be.kdg.prog6.waterside.adapter.out.db.entity;

import be.kdg.prog6.waterside.domain.ShippingOrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "waterside", name = "shipping_orders")
public class ShippingOrderJpaEntity {
    @Id
    @Column(name = "shipping_order_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID shippingOrderId;

    @Column(name = "reference_id", columnDefinition = "varchar(36)", unique = true)
    // unique because a PO can only be referenced by a single SO
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID referenceId;

    @Column(name = "buyer_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID buyerId;

    @Column(name = "vessel_number")
    private String vesselNumber;

    @Column(name = "scheduled_arrival_date")
    private LocalDateTime scheduledArrivalDate;

    @Column(name = "scheduled_departure_date")
    private LocalDateTime scheduledDepartureDate;

    @Column(name = "actual_arrival_date")
    private LocalDateTime actualArrivalDate;

    @Column(name = "actual_departure_date")
    private LocalDateTime actualDepartureDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShippingOrderStatus status;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "inspection_operation_id")
    private InspectionOperationJpaEntity inspectionOperation;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bunkering_operation_id")
    private BunkeringOperationJpaEntity bunkeringOperation;

    public ShippingOrderJpaEntity() {}

    public UUID getShippingOrderId() {
        return shippingOrderId;
    }

    public void setShippingOrderId(final UUID shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(final UUID referenceId) {
        this.referenceId = referenceId;
    }

    public UUID getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(final UUID buyerId) {
        this.buyerId = buyerId;
    }

    public String getVesselNumber() {
        return vesselNumber;
    }

    public void setVesselNumber(final String vesselNumber) {
        this.vesselNumber = vesselNumber;
    }

    public LocalDateTime getScheduledArrivalDate() {
        return scheduledArrivalDate;
    }

    public void setScheduledArrivalDate(final LocalDateTime scheduledArrivalDate) {
        this.scheduledArrivalDate = scheduledArrivalDate;
    }

    public LocalDateTime getScheduledDepartureDate() {
        return scheduledDepartureDate;
    }

    public void setScheduledDepartureDate(final LocalDateTime scheduledDepartureDate) {
        this.scheduledDepartureDate = scheduledDepartureDate;
    }

    public LocalDateTime getActualArrivalDate() {
        return actualArrivalDate;
    }

    public void setActualArrivalDate(final LocalDateTime actualArrivalDate) {
        this.actualArrivalDate = actualArrivalDate;
    }

    public LocalDateTime getActualDepartureDate() {
        return actualDepartureDate;
    }

    public void setActualDepartureDate(final LocalDateTime actualDepartureDate) {
        this.actualDepartureDate = actualDepartureDate;
    }

    public ShippingOrderStatus getStatus() {
        return status;
    }

    public void setStatus(final ShippingOrderStatus status) {
        this.status = status;
    }

    public InspectionOperationJpaEntity getInspectionOperation() {
        return inspectionOperation;
    }

    public void setInspectionOperation(final InspectionOperationJpaEntity inspectionOperation) {
        this.inspectionOperation = inspectionOperation;
    }

    public BunkeringOperationJpaEntity getBunkeringOperation() {
        return bunkeringOperation;
    }

    public void setBunkeringOperation(final BunkeringOperationJpaEntity bunkeringOperation) {
        this.bunkeringOperation = bunkeringOperation;
    }
}
