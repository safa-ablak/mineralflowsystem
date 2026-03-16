package be.kdg.prog6.landside.domain;

import be.kdg.prog6.landside.domain.exception.EarlyArrivalException;
import be.kdg.prog6.landside.domain.exception.LateArrivalException;

import java.time.Duration;
import java.time.LocalDateTime;

import static be.kdg.prog6.common.ProjectInfo.KDG;

public class Appointment {
    /*
     * Attribute(s)
     */
    private static final Duration ARRIVAL_WINDOW_DURATION = Duration.ofMinutes(60);

    private final AppointmentId appointmentId;
    private final SupplierId supplierId;
    private final WarehouseId warehouseId;
    private final TruckLicensePlate truckLicensePlate;
    private final RawMaterial rawMaterial;
    private final LocalDateTime arrivalWindowStart; // arrivalWindowEnd = (arrivalWindowStart + ARRIVAL_WINDOW_DURATION)
    private AppointmentStatus status;

    /*
     * Constructor(s)
     */
    public Appointment(final SupplierId supplierId,
                       final TruckLicensePlate truckLicensePlate, final RawMaterial rawMaterial,
                       final LocalDateTime arrivalWindowStart, final WarehouseId warehouseId) {
        this.appointmentId = AppointmentId.newId();
        this.supplierId = supplierId;
        this.truckLicensePlate = truckLicensePlate;
        this.rawMaterial = rawMaterial;
        this.arrivalWindowStart = arrivalWindowStart;
        this.warehouseId = warehouseId;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public Appointment(final AppointmentId appointmentId, final SupplierId supplierId,
                       final WarehouseId warehouseId,
                       final TruckLicensePlate truckLicensePlate, final RawMaterial rawMaterial,
                       final LocalDateTime arrivalWindowStart, final AppointmentStatus status) {
        this.appointmentId = appointmentId;
        this.supplierId = supplierId;
        this.warehouseId = warehouseId;
        this.truckLicensePlate = truckLicensePlate;
        this.rawMaterial = rawMaterial;
        this.arrivalWindowStart = arrivalWindowStart;
        this.status = status;
    }

    /*
     * Method(s)
     */
    public void ensureWithinArrivalWindow(final LocalDateTime arrivalTime) {
        if (isEarlyArrival(arrivalTime)) {
            throw new EarlyArrivalException("Truck arrival is early, please check your Appointment arrival window.");
        }
        if (isLateArrival(arrivalTime)) {
            throw new LateArrivalException("Truck arrival is late, please book a new Appointment with %s.".formatted(KDG));
        }
    }

    private boolean isLateArrival(final LocalDateTime arrivalTime) {
        return arrivalTime.isAfter(getArrivalWindowEnd());
    }

    private boolean isEarlyArrival(final LocalDateTime arrivalTime) {
        return arrivalTime.isBefore(arrivalWindowStart);
    }

    public boolean isActive() {
        return this.status != AppointmentStatus.CANCELLED;
    }

    public boolean isScheduled() {
        return this.status == AppointmentStatus.SCHEDULED;
    }

    public boolean isCancelled() {
        return this.status == AppointmentStatus.CANCELLED;
    }

    void cancel() {
        setStatus(AppointmentStatus.CANCELLED);
    }

    void fulfill() {
        setStatus(AppointmentStatus.FULFILLED);
    }

    public boolean isFulfilled() {
        return this.status == AppointmentStatus.FULFILLED;
    }

    /*
     * Getter(s)/Setter(s)
     */
    public AppointmentId getAppointmentId() {
        return appointmentId;
    }

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public WarehouseId getWarehouseId() {
        return warehouseId;
    }

    public TruckLicensePlate getTruckLicensePlate() {
        return truckLicensePlate;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public LocalDateTime getArrivalWindowStart() {
        return arrivalWindowStart;
    }

    public LocalDateTime getArrivalWindowEnd() {
        return arrivalWindowStart.plus(ARRIVAL_WINDOW_DURATION);
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    private void setStatus(final AppointmentStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof Appointment that)) return false;
        return appointmentId.equals(that.appointmentId);
    }

    @Override
    public int hashCode() {
        return appointmentId.hashCode();
    }

    @Override
    public String toString() {
        return """
            Appointment {
                ID:              %s
                Supplier ID:     %s
                Warehouse ID:    %s
                License Plate:   %s
                Raw Material:    %s
                Arrival Window:  %s -> %s
                Status:          %s
            }""".formatted(
            appointmentId.id(),
            supplierId.id(),
            warehouseId.id(),
            truckLicensePlate.value(),
            rawMaterial,
            arrivalWindowStart,
            getArrivalWindowEnd(),
            status
        );
    }
}
