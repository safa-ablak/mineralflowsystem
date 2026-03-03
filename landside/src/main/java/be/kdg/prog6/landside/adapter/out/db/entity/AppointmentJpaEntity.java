package be.kdg.prog6.landside.adapter.out.db.entity;

import be.kdg.prog6.landside.domain.AppointmentStatus;
import be.kdg.prog6.landside.domain.RawMaterial;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "landside", name = "appointments")
public class AppointmentJpaEntity {
    @Id
    @Column(name = "appointment_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID appointmentId;

    @ManyToOne
    @JoinColumn(name = "time_slot_id", referencedColumnName = "id")
    private TimeSlotJpaEntity timeSlot;

    @Column(name = "supplier_id", columnDefinition = "varchar(36)", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID supplierId;

    @Column(name = "warehouse_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID warehouseId;

    @Column(name = "truck_license_plate", nullable = false)
    private String truckLicensePlate;

    @Enumerated(EnumType.STRING)
    @Column(name = "raw_material", nullable = false)
    private RawMaterial rawMaterial;

    @Column(name = "arrival_window_start", nullable = false)
    private LocalDateTime arrivalWindowStart;

    @Column(name = "arrival_window_end", nullable = false)
    private LocalDateTime arrivalWindowEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    public AppointmentJpaEntity() {
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(final UUID id) {
        this.appointmentId = id;
    }

    public TimeSlotJpaEntity getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlotJpaEntity timeSlot) {
        this.timeSlot = timeSlot;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(final UUID supplierId) {
        this.supplierId = supplierId;
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

    public LocalDateTime getArrivalWindowStart() {
        return arrivalWindowStart;
    }

    public void setArrivalWindowStart(final LocalDateTime arrivalWindowStart) {
        this.arrivalWindowStart = arrivalWindowStart;
    }

    public LocalDateTime getArrivalWindowEnd() {
        return arrivalWindowEnd;
    }

    public void setArrivalWindowEnd(final LocalDateTime arrivalWindowEnd) {
        this.arrivalWindowEnd = arrivalWindowEnd;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(final AppointmentStatus status) {
        this.status = status;
    }
}
