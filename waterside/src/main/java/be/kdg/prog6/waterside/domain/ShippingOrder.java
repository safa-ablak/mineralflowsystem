package be.kdg.prog6.waterside.domain;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.waterside.domain.exception.bunkering.BunkeringAlreadyCompletedException;
import be.kdg.prog6.waterside.domain.exception.inspection.InspectionAlreadyCompletedException;

import java.time.LocalDateTime;

public class ShippingOrder {
    /*
     * Attribute(s)
     */
    private final ShippingOrderId shippingOrderId; // Unique identifier for the Shipping Order
    private ReferenceId referenceId; // Referenced Purchase Order identifier
    private BuyerId buyerId; // Buyer ID, should match to that of the PO in the Warehousing BC
    // Vessel details
    private VesselNumber vesselNumber;
    // Arrival/Departure dates
    private final LocalDateTime scheduledArrivalDate;
    private final LocalDateTime scheduledDepartureDate;
    private LocalDateTime actualArrivalDate; // To be filled when the ship docks at the port
    private LocalDateTime actualDepartureDate; // To be filled when the ship leaves the port
    // Inspection Operation
    private final InspectionOperation inspection;
    //    private final LocalDate inspectionDate;
//    private final String inspectorSignature;
    // Bunkering Operation
    private final BunkeringOperation bunkering;
    //    private final LocalDateTime bunkeringDate;
    // Status
    private ShippingOrderStatus status;

    /*
     * Constructor(s)
     */
    public ShippingOrder(final BuyerId buyerId, final ReferenceId referenceId, final VesselNumber vesselNumber,
                         final LocalDateTime scheduledArrivalDate, final LocalDateTime scheduledDepartureDate) {
        this.shippingOrderId = ShippingOrderId.newId();
        this.buyerId = buyerId;
        this.referenceId = referenceId;
        this.vesselNumber = vesselNumber;
        this.scheduledArrivalDate = scheduledArrivalDate;
        this.scheduledDepartureDate = scheduledDepartureDate;
        this.actualArrivalDate = null;
        this.actualDepartureDate = null;
        this.inspection = new InspectionOperation();
        this.bunkering = new BunkeringOperation();
        this.status = ShippingOrderStatus.PENDING_VALIDATION; // Initial state when the shipping order is entered
    }

    public ShippingOrder(final ShippingOrderId shippingOrderId, final ReferenceId referenceId,
                         final VesselNumber vesselNumber, final BuyerId buyerId,
                         final LocalDateTime scheduledArrivalDate, final LocalDateTime scheduledDepartureDate,
                         final LocalDateTime actualArrivalDate, final LocalDateTime actualDepartureDate,
                         final ShippingOrderStatus status,
                         final InspectionOperation inspection,
                         final BunkeringOperation bunkering) {
        this.shippingOrderId = shippingOrderId;
        this.referenceId = referenceId;
        this.vesselNumber = vesselNumber;
        this.buyerId = buyerId;
        this.scheduledArrivalDate = scheduledArrivalDate;
        this.scheduledDepartureDate = scheduledDepartureDate;
        this.actualArrivalDate = actualArrivalDate;
        this.actualDepartureDate = actualDepartureDate;
        this.inspection = inspection;
        this.bunkering = bunkering;
        this.status = status;
    }

    /*
     * Getter(s)/Setter(s)
     */
    public BuyerId getBuyerId() {
        return buyerId;
    }

    private void setVesselNumber(final VesselNumber vesselNumber) {
        this.vesselNumber = vesselNumber;
    }

    public ShippingOrderId getShippingOrderId() {
        return shippingOrderId;
    }

    public ReferenceId getReferenceId() {
        return referenceId;
    }

    private void setReferenceId(final ReferenceId referenceId) {
        this.referenceId = referenceId;
    }

    private void setBuyerId(final BuyerId buyerId) {
        this.buyerId = buyerId;
    }

    public VesselNumber getVesselNumber() {
        return vesselNumber;
    }

    public LocalDateTime getScheduledArrivalDate() {
        return scheduledArrivalDate;
    }

    public LocalDateTime getScheduledDepartureDate() {
        return scheduledDepartureDate;
    }

    public LocalDateTime getActualArrivalDate() {
        return actualArrivalDate;
    }

    private void setActualArrivalDate(final LocalDateTime actualArrivalDate) {
        this.actualArrivalDate = actualArrivalDate;
    }

    public LocalDateTime getActualDepartureDate() {
        return actualDepartureDate;
    }

    private void setActualDepartureDate(final LocalDateTime actualDepartureDate) {
        this.actualDepartureDate = actualDepartureDate;
    }

    public ShippingOrderStatus getStatus() {
        return status;
    }

    private void setStatus(final ShippingOrderStatus status) {
        this.status = status;
    }

    public InspectionOperation getInspection() {
        return inspection;
    }

    public BunkeringOperation getBunkering() {
        return bunkering;
    }

    /*
     * State Queries/Transitions
     */
    public boolean isPendingValidation() {
        return this.status == ShippingOrderStatus.PENDING_VALIDATION;
    }

    public void match() {
        setStatus(ShippingOrderStatus.MATCHED);
    }

    public boolean isMatched() {
        return this.status == ShippingOrderStatus.MATCHED;
    }

    public boolean isShipDocked() {
        return this.status == ShippingOrderStatus.SHIP_DOCKED;
    }

    public void amendDetails(final BuyerId newBuyerId, final ReferenceId newReferenceId) {
        ensureCanBeAmended();
        if (newBuyerId != null) {
            setBuyerId(newBuyerId);
        }
        if (newReferenceId != null) {
            setReferenceId(newReferenceId);
        }
    }

    public boolean isShipInspected() {
        return this.status == ShippingOrderStatus.SHIP_INSPECTED;
    }

    public boolean isShipBunkered() {
        return this.status == ShippingOrderStatus.SHIP_BUNKERED;
    }

    public boolean isShipDeparted() {
        return this.status == ShippingOrderStatus.SHIP_DEPARTED;
    }

    private boolean areMandatoryOperationsCompleted() {
        return inspection.isCompleted() && bunkering.isCompleted();
    }

    /*
     * As soon as mandatory tasks are done (BO ended) and (IO successfully ended), the ship is
     * loaded (loading slip issued [Out Of Scope]) the ship will leave.
     * */
    public boolean isReadyForLoading() {
        return areMandatoryOperationsCompleted();
    }

    /*
     * Guards
     */
    private void ensureCanBeAmended() {
        if (!isPendingValidation()) {
            throw new InvalidOperationException("Shipping Order can only be amended before validation");
        }
    }

    private void ensureInspectionNotCompleted() {
        if (inspection.isCompleted()) {
            throw InspectionAlreadyCompletedException.forShippingOrder(shippingOrderId);
        }
    }

    private void ensureBunkeringNotCompleted() {
        if (bunkering.isCompleted()) {
            throw BunkeringAlreadyCompletedException.forShippingOrder(shippingOrderId);
        }
    }

    public void ensureCanInitiateLoading() {
        if (!areMandatoryOperationsCompleted()) {
            throw new InvalidOperationException("Cannot load shipping order before mandatory operations (IO + BO) are completed.");
        }
        if (isShipDeparted()) {
            throw new InvalidOperationException("Cannot load shipping order after ship has already departed.");
        }
    }

    private void ensureShipNotDeparted() {
        if (isShipDeparted()) {
            throw new InvalidOperationException("The ship has already departed.");
        }
    }

    private void ensureAllOperationsCompleted() {
        if (!areMandatoryOperationsCompleted()) {
            throw new InvalidOperationException("Cannot depart the ship until all operations are completed.");
        }
    }

    public void dockShip() {
        ensureShipNotDeparted();
        setActualArrivalDate(LocalDateTime.now());
        this.status = ShippingOrderStatus.SHIP_DOCKED;
    }

    public void performInspection(final String inspectorSignature) {
        ensureShipNotDeparted();
        ensureInspectionNotCompleted();
        inspection.complete(inspectorSignature);
        this.status = ShippingOrderStatus.SHIP_INSPECTED;
    }

    public void performBunkering() {
        ensureShipNotDeparted();
        ensureBunkeringNotCompleted();
        bunkering.complete();
        this.status = ShippingOrderStatus.SHIP_BUNKERED;
    }

    public void departShip() {
        ensureAllOperationsCompleted();
        setActualDepartureDate(LocalDateTime.now());
        this.status = ShippingOrderStatus.SHIP_DEPARTED;
    }

    @Override
    public String toString() {
        return "ShippingOrder{shippingOrderId=%s, referenceId=%s, vesselNumber=%s, scheduledArrivalDate=%s, scheduledDepartureDate=%s, actualArrivalDate=%s, actualDepartureDate=%s, status=%s}".formatted(
            shippingOrderId.id(), referenceId.id(), vesselNumber.value(), scheduledArrivalDate, scheduledDepartureDate, actualArrivalDate, actualDepartureDate, status
        );
    }
}
