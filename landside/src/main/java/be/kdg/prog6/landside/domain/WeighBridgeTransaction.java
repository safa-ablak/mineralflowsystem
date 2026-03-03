package be.kdg.prog6.landside.domain;

import be.kdg.prog6.common.exception.InvalidOperationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class WeighBridgeTransaction {
    /**
     * Attribute(s)
     */
    private final WeighBridgeTransactionId id;
    private final TruckLicensePlate truckLicensePlate;
    private final BigDecimal grossWeight; // weigh-in
    private BigDecimal tareWeight; // weigh-out
    private final LocalDateTime weighInTime; // declared final it's initialized at creation time
    private LocalDateTime weighOutTime; // Will be set at the time of the weigh-out

    /**
     * Constructor(s)
     */
    public WeighBridgeTransaction(final WeighBridgeTransactionId id, final TruckLicensePlate truckLicensePlate,
                                  final BigDecimal grossWeight, final BigDecimal tareWeight,
                                  final LocalDateTime weighInTime, final LocalDateTime weighOutTime) {
        this.id = id;
        this.truckLicensePlate = truckLicensePlate;
        this.grossWeight = grossWeight;
        this.tareWeight = tareWeight;
        this.weighInTime = weighInTime;
        this.weighOutTime = weighOutTime;
    }

    public WeighBridgeTransaction(final TruckLicensePlate truckLicensePlate, final BigDecimal grossWeight, final LocalDateTime weighInTime) {
        this.id = WeighBridgeTransactionId.of(UUID.randomUUID());
        this.truckLicensePlate = truckLicensePlate;
        this.grossWeight = grossWeight;
        this.tareWeight = null;
        this.weighInTime = weighInTime;
        this.weighOutTime = null;
    }

    /**
     * Method(s)
     */
    // Record the second weighing (weigh-out) with tare weight and the time it occurred
    void recordWeighOut(final BigDecimal tareWeight, final LocalDateTime weighOutTime) {
        if (isWeighOutRecorded()) {
            throw new InvalidOperationException("Weigh-out has already been recorded.");
        }
        if (tareWeight.compareTo(grossWeight) > 0) {
            throw new InvalidOperationException("Tare weight cannot be greater than the gross weight.");
        }
        setTareWeight(tareWeight);
        setWeighOutTime(weighOutTime);
    }

    public BigDecimal calculateNetWeight() {
        if (!isWeighOutRecorded()) {
            throw new InvalidOperationException("Tare weight has not been recorded yet.");
        }
        return grossWeight.subtract(tareWeight);  // Net weight = Gross weight - Tare weight
    }

    public boolean isWeighOutRecorded() {
        return tareWeight != null && weighOutTime != null;
    }

    /**
     * Getter(s)/Setter(s)
     */
    public WeighBridgeTransactionId getId() {
        return id;
    }

    public TruckLicensePlate getTruckLicensePlate() {
        return truckLicensePlate;
    }

    public BigDecimal getGrossWeight() {
        return grossWeight;
    }

    public BigDecimal getTareWeight() {
        return tareWeight;
    }

    private void setTareWeight(final BigDecimal tareWeight) {
        this.tareWeight = tareWeight;
    }

    public LocalDateTime getWeighInTime() {
        return weighInTime;
    }

    public LocalDateTime getWeighOutTime() {
        return weighOutTime;
    }

    private void setWeighOutTime(final LocalDateTime weighOutTime) {
        this.weighOutTime = weighOutTime;
    }
}
