package be.kdg.prog6.landside.domain;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.landside.domain.event.TruckDepartedEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Visit {
    /*
     * Attribute(s)
     */
    private final VisitId visitId;
    private final AppointmentId appointmentId; // Not storing the appointment itself, but referencing it via its identifier.
    private final WarehouseId warehouseId; // To be able to know which warehouse truck will be docked at, captured from Appointment.
    // Captured from Appointment at time of visit creation.
    // Purpose: simplify querying and tracking visit lifecycle by truck.
    private final TruckLicensePlate truckLicensePlate;
    private final RawMaterial rawMaterial;
    private final LocalDateTime arrivalTime; // start of the visit
    private WeighBridgeTransaction weighBridgeTransaction;
    private LocalDateTime dockTime; // when the truck docks to the conveyor belt.
    private LocalDateTime departureTime;
    private VisitStatus status;

    /*
     * Constructor(s)
     */
    public Visit(final VisitId visitId, final AppointmentId appointmentId,
                 final WarehouseId warehouseId, final TruckLicensePlate truckLicensePlate,
                 final RawMaterial rawMaterial,
                 final LocalDateTime arrivalTime, final WeighBridgeTransaction weighBridgeTransaction,
                 final LocalDateTime dockTime, final LocalDateTime departureTime,
                 final VisitStatus status) {
        this.visitId = visitId;
        this.appointmentId = appointmentId;
        this.warehouseId = warehouseId;
        this.truckLicensePlate = truckLicensePlate;
        this.rawMaterial = rawMaterial;
        this.arrivalTime = arrivalTime;
        this.weighBridgeTransaction = weighBridgeTransaction;
        this.dockTime = dockTime;
        this.departureTime = departureTime;
        this.status = status;
    }

    public Visit(final AppointmentId appointmentId, final WarehouseId warehouseId,
                 final TruckLicensePlate truckLicensePlate, final RawMaterial rawMaterial,
                 final LocalDateTime arrivalTime) {
        this.visitId = VisitId.newId();
        this.appointmentId = appointmentId;
        this.warehouseId = warehouseId;
        this.truckLicensePlate = truckLicensePlate;
        this.rawMaterial = rawMaterial;
        this.arrivalTime = arrivalTime;
        this.weighBridgeTransaction = null;
        this.status = VisitStatus.ENTERED_FACILITY;
    }

    /*
     * Method(s)
     */
    public boolean isEnteredFacility() {
        return this.status == VisitStatus.ENTERED_FACILITY;
    }

    public boolean isWeighedIn() {
        return this.status == VisitStatus.WEIGHED_IN;
    }

    public void dockTruck() {
        if (dockTimeRecorded()) {
            throw new InvalidOperationException("Docking time already recorded for this visit.");
        }
        this.status = VisitStatus.DOCKED;
        setDockTime(LocalDateTime.now());
    }

    public boolean isDocked() {
        return this.status == VisitStatus.DOCKED;
    }

    public boolean isWeighedOut() {
        return this.status == VisitStatus.WEIGHED_OUT;
    }

    public boolean isCompleted() {
        return this.status == VisitStatus.COMPLETED;
    }

    // Method to check if the truck is on-site => incomplete visit
    public boolean isTruckOnSite() {
        return !isCompleted();
    }

    public WeighBridgeTransaction recordWeighIn(final BigDecimal grossWeight) {
        if (hasWeighBridgeTransaction()) {
            throw new InvalidOperationException("Weigh-in already recorded for this visit.");
        }

        weighBridgeTransaction = createWeighBridgeTransaction(truckLicensePlate, grossWeight);
        this.status = VisitStatus.WEIGHED_IN;

        return weighBridgeTransaction;
    }

    private WeighBridgeTransaction createWeighBridgeTransaction(final TruckLicensePlate truckLicensePlate, final BigDecimal grossWeight) {
        return new WeighBridgeTransaction(truckLicensePlate, grossWeight, LocalDateTime.now());
    }

    public WeighBridgeTransaction recordWeighOut(final BigDecimal tareWeight) {
        if (!hasWeighBridgeTransaction()) {
            throw new InvalidOperationException("Weigh-In must be recorded before Weigh-Out.");
        }

        weighBridgeTransaction.recordWeighOut(tareWeight, LocalDateTime.now());
        this.status = VisitStatus.WEIGHED_OUT;

        return weighBridgeTransaction;
    }

    public boolean dockTimeRecorded() {
        return dockTime != null;
    }

    public boolean hasWeighBridgeTransaction() {
        return weighBridgeTransaction != null;
    }

    public TruckDepartedEvent complete() {
        setDepartureTime(LocalDateTime.now());
        this.status = VisitStatus.COMPLETED;
        // Return domain event that the truck has departed
        return new TruckDepartedEvent(truckLicensePlate, appointmentId);
    }

    /*
     * Getter(s)/Setter(s)
     */
    public VisitId getVisitId() {
        return visitId;
    }

    public AppointmentId getAppointmentId() {
        return appointmentId;
    }

    public TruckLicensePlate getTruckLicensePlate() {
        return truckLicensePlate;
    }

    public WeighBridgeTransaction getWeighBridgeTransaction() {
        return weighBridgeTransaction;
    }

    private void setWeighBridgeTransaction(final WeighBridgeTransaction weighBridgeTransaction) {
        this.weighBridgeTransaction = weighBridgeTransaction;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public LocalDateTime getDockTime() {
        return dockTime;
    }

    private void setDockTime(final LocalDateTime dockTime) {
        this.dockTime = dockTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    private void setDepartureTime(final LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public VisitStatus getStatus() {
        return status;
    }

    private void setStatus(final VisitStatus status) {
        this.status = status;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public WarehouseId getWarehouseId() {
        return warehouseId;
    }
}
