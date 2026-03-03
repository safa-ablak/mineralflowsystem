package be.kdg.prog6.invoicing.domain;

/// Raw Material is a persisted entity in the Invoicing Ctx, to be able to keep track
/// of real-time pricing(storage price/ton/day, unit price/ton) of raw materials.
public class RawMaterial {
    private final RawMaterialId id;
    private final String name;
    private Money storagePricePerTonPerDay;
    private Money unitPricePerTon;

    public RawMaterial(final String name,
                       final Money storagePricePerTonPerDay,
                       final Money unitPricePerTon) {
        this.id = RawMaterialId.newId();
        this.name = name;
        this.storagePricePerTonPerDay = storagePricePerTonPerDay;
        this.unitPricePerTon = unitPricePerTon;
    }

    public RawMaterial(final RawMaterialId id,
                       final String name,
                       final Money storagePricePerTonPerDay,
                       final Money unitPricePerTon) {
        this.id = id;
        this.name = name;
        this.storagePricePerTonPerDay = storagePricePerTonPerDay;
        this.unitPricePerTon = unitPricePerTon;
    }

    public RawMaterialId getId() {
        return id;
    }

    /**
     * Gets the name of the raw material.
     *
     * @return Name of the raw material.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the storage price per-ton per-day.
     *
     * @return Storage price per-ton per-day as a Money object.
     */
    public Money getStoragePricePerTonPerDay() {
        return storagePricePerTonPerDay;
    }

    /**
     * Gets the unit price per ton.
     *
     * @return Unit price per ton as a Money object.
     */
    public Money getUnitPricePerTon() {
        return unitPricePerTon;
    }

    // Assuming there is no business logic for modification
    public void modifyStoragePricePerTonPerDay(final Money newStoragePricePerTonPerDay) {
        setStoragePricePerTonPerDay(newStoragePricePerTonPerDay);
    }

    // Assuming there is no business logic for modification
    public void modifyUnitPricePerTon(final Money newUnitPricePerTon) {
        setUnitPricePerTon(newUnitPricePerTon);
    }

    private void setStoragePricePerTonPerDay(final Money storagePricePerTonPerDay) {
        this.storagePricePerTonPerDay = storagePricePerTonPerDay;
    }

    private void setUnitPricePerTon(final Money unitPricePerTon) {
        this.unitPricePerTon = unitPricePerTon;
    }
}