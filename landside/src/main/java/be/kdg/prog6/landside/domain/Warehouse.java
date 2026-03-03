package be.kdg.prog6.landside.domain;

import be.kdg.prog6.landside.domain.exception.WarehouseNotAvailableException;

import java.math.BigDecimal;

public class Warehouse {
    // Maximum capacity percentage for scheduling new appointments (80%)
    private static final BigDecimal MAX_APPOINTMENT_CAPACITY_PERCENTAGE = BigDecimal.valueOf(80.0);

    private final WarehouseId warehouseId;
    private boolean isAvailable; // We know this via data projection from the Warehousing BC
    private RawMaterial rawMaterial;
    private final SupplierId supplierId;

    public Warehouse(final WarehouseId warehouseId, final boolean isAvailable, final RawMaterial rawMaterial, final SupplierId supplierId) {
        this.warehouseId = warehouseId;
        this.isAvailable = isAvailable;
        this.rawMaterial = rawMaterial;
        this.supplierId = supplierId;
    }

    public WarehouseId getWarehouseId() {
        return warehouseId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void updateAvailability(final BigDecimal percentageFilled) {
        if (percentageFilled.compareTo(MAX_APPOINTMENT_CAPACITY_PERCENTAGE) >= 0) {
            setAvailable(false);
            return;
        }
        setAvailable(true);
    }

    private void setAvailable(final boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(final RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public SupplierId getSupplierId() {
        return supplierId;
    }

    public void ensureAvailableForNewAppointment() {
        if (!isAvailable) {
            throw WarehouseNotAvailableException.forNewAppointment(
                warehouseId, supplierId, rawMaterial
            );
        }
    }
}
