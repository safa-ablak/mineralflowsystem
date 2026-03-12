package be.kdg.prog6.landside.adapter.out.db.entity;

import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.VisitStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "landside", name = "visits")
public class VisitJpaEntity {
    @Id
    @Column(name = "visit_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID visitId;

    @Column(name = "appointment_id", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID appointmentId;

    @Column(name = "warehouse_id", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID warehouseId;

    @Column(name = "truck_license_plate", nullable = false)
    private String truckLicensePlate;

    @Column(name = "raw_material", nullable = false)
    private RawMaterial rawMaterial;

    /**
     * The <b>weighbridge transaction</b> associated with this visit.
     * <b>Cascade all</b> to allow both creation and updates of the weighbridge transaction.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "weigh_bridge_transaction_id", nullable = true)
    private WeighBridgeTransactionJpaEntity weighBridgeTransaction;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "dock_time", nullable = true)
    private LocalDateTime dockTime;

    @Column(name = "departure_time", nullable = true)
    private LocalDateTime departureTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VisitStatus status;

    public VisitJpaEntity() {}

    public VisitJpaEntity(final UUID visitId) {
        this.visitId = visitId;
    }

    public UUID getVisitId() {
        return visitId;
    }

    public void setVisitId(final UUID visitId) {
        this.visitId = visitId;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(final UUID appointmentId) {
        this.appointmentId = appointmentId;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(final UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getTruckLicensePlate() {
        return truckLicensePlate;
    }

    public void setTruckLicensePlate(final String truckLicensePlate) {
        this.truckLicensePlate = truckLicensePlate;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(final RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public WeighBridgeTransactionJpaEntity getWeighBridgeTransaction() {
        return weighBridgeTransaction;
    }

    public void setWeighBridgeTransaction(final WeighBridgeTransactionJpaEntity weighBridgeTransaction) {
        this.weighBridgeTransaction = weighBridgeTransaction;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(final LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getDockTime() {
        return dockTime;
    }

    public void setDockTime(final LocalDateTime dockTime) {
        this.dockTime = dockTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(final LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public VisitStatus getStatus() {
        return status;
    }

    public void setStatus(final VisitStatus status) {
        this.status = status;
    }
}
