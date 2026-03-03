package be.kdg.prog6.landside.domain;

import be.kdg.prog6.landside.domain.exception.WeighBridgeOccupiedException;

import java.util.Objects;

public class WeighBridge {
    private final WeighBridgeId id;
    private final WeighBridgeNumber number;
    private VisitId occupiedByVisitId;

    public WeighBridge(final WeighBridgeNumber number) {
        this.id = WeighBridgeId.newId();
        this.number = number;
        this.occupiedByVisitId = null;
    }

    public WeighBridge(final WeighBridgeId id, final WeighBridgeNumber number, final VisitId occupiedByVisitId) {
        this.id = id;
        this.number = number;
        this.occupiedByVisitId = occupiedByVisitId;
    }

    public void occupy(final VisitId visitId) {
        if (isOccupied()) {
            throw new WeighBridgeOccupiedException(number);
        }
        this.occupiedByVisitId = visitId;
    }

    public void release() {
        this.occupiedByVisitId = null;
    }

    public boolean isOccupied() {
        return occupiedByVisitId != null;
    }

    public boolean isAvailable() {
        return !isOccupied();
    }

    public WeighBridgeId getId() {
        return id;
    }

    public WeighBridgeNumber getNumber() {
        return number;
    }

    public VisitId getOccupiedByVisitId() {
        return occupiedByVisitId;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final WeighBridge that = (WeighBridge) other;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}