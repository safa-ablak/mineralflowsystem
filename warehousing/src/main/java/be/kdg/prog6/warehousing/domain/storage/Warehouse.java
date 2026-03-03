package be.kdg.prog6.warehousing.domain.storage;

import be.kdg.prog6.common.domain.measurement.Weight;
import be.kdg.prog6.common.domain.measurement.WeightUnit;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.exception.storage.DeliveryCapacityExceededException;
import be.kdg.prog6.warehousing.domain.exception.storage.InsufficientStockException;
import be.kdg.prog6.warehousing.domain.exception.storage.RawMaterialConflictException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Warehouse {
    // Regular capacity of the warehouse in tons (500 kt = 500_000 t)
    private static final Weight REGULAR_CAPACITY = new Weight(WeightUnit.TONS, BigDecimal.valueOf(500_000));
    // Maximum capacity ratio including overflow for accepting deliveries (110%)
    private static final BigDecimal MAX_OVERFLOW_CAPACITY_RATIO = BigDecimal.valueOf(1.10);

    private final WarehouseId warehouseId;
    private final WarehouseNumber warehouseNumber; // e.g., WH-01
    private final SellerId sellerId;
    private RawMaterial rawMaterial;
    private Balance baseBalance;
    private final StockLedger stockLedger;
    private final SiteLocation siteLocation;

    /*
     * Constructor(s)
     */
    // The warehouse is initially empty and the raw material type is unknown until the first dump.
    public Warehouse(final SellerId sellerId, final WarehouseNumber warehouseNumber, final SiteLocation siteLocation) {
        this.warehouseId = WarehouseId.newId();
        this.sellerId = sellerId;
        this.warehouseNumber = warehouseNumber;
        this.rawMaterial = null; // At this point it's unknown
        this.baseBalance = new Balance(LocalDateTime.now(), BigDecimal.ZERO); // Warehouse is initially empty
        this.stockLedger = StockLedger.emptyFor(warehouseId); // No deliveries or shipments yet
        this.siteLocation = siteLocation;
    }

    public Warehouse(final WarehouseId warehouseId, final SellerId sellerId,
                     final WarehouseNumber warehouseNumber, final RawMaterial rawMaterial,
                     final Balance baseBalance, final StockLedger stockLedger, final SiteLocation siteLocation) {
        this.warehouseId = warehouseId;
        this.sellerId = sellerId;
        this.warehouseNumber = warehouseNumber;
        this.rawMaterial = rawMaterial;
        this.baseBalance = baseBalance;
        this.stockLedger = initializeStockLedger(stockLedger);
        this.siteLocation = siteLocation;
    }

    private StockLedger initializeStockLedger(final StockLedger stockLedger) {
        return stockLedger != null ? stockLedger : StockLedger.emptyFor(warehouseId);
    }

    Balance calculateBalance() {
        return stockLedger.calculateBalance(baseBalance);
    }

    public StockLevel calculateStockLevel() {
        return StockLevel.from(calculateBalance(), REGULAR_CAPACITY.amount());
    }

    public boolean isEmpty() {
        return calculateBalance().isZero();
    }

    /*
     *  DELIVERY: For deliveries via trucks
     */

    /**
     * Records a new Delivery in the {@link StockLedger}.
     *
     * @param amountToDeliver the delivered amount
     * @return the newly created {@link Delivery} entry
     * @throws DeliveryCapacityExceededException if adding the delivery would exceed the warehouse capacity
     */
    public Delivery recordDelivery(final BigDecimal amountToDeliver) {
        ensureCanAcceptDelivery(amountToDeliver);
        return stockLedger.recordDelivery(amountToDeliver);
    }

    private void ensureCanAcceptDelivery(final BigDecimal amountToDeliver) {
        if (!canAcceptDelivery(amountToDeliver)) {
            final BigDecimal maxOverflowCapacity = calculateMaxOverflowCapacity();
            throw DeliveryCapacityExceededException.forDelivery(warehouseId, amountToDeliver, maxOverflowCapacity);
        }
    }

    private boolean canAcceptDelivery(final BigDecimal amount) {
        final BigDecimal currentBalance = balance();
        final BigDecimal projectedBalance = currentBalance.add(amount);
        final BigDecimal maxOverflowCapacity = calculateMaxOverflowCapacity();
        return projectedBalance.compareTo(maxOverflowCapacity) <= 0;
    }

    /*
     *  SHIPMENT: For shipments via ships
     */

    /**
     * Ships from the shippable deliveries, starting with the oldest (FIFO).
     *
     * @param amountToShip the amount to ship
     * @return a {@link ShipmentRecord} capturing the Shipment and the specific Delivery allocations
     * that supplied it, as recorded in the {@link StockLedger}
     * @throws InsufficientStockException if there is not enough stock to ship
     */
    public ShipmentRecord recordShipment(final BigDecimal amountToShip) {
        ensureSufficientStock(amountToShip);
        return stockLedger.recordShipment(amountToShip);
    }

    private void ensureSufficientStock(final BigDecimal amountToShip) {
        if (!canProcessShipment(amountToShip)) {
            final BigDecimal shortfall = amountToShip.subtract(balance());
            throw InsufficientStockException.forShipment(warehouseId, amountToShip, shortfall);
        }
    }

    private boolean canProcessShipment(final BigDecimal amount) {
        final BigDecimal currentBalance = balance();
        final BigDecimal projectedBalance = currentBalance.subtract(amount);
        return projectedBalance.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * See {@code docs/notes-and-assumptions-made.md} -> "Warehousing Context" for details.
     */
    public void assignRawMaterial(final RawMaterial toAssign) {
        if (isRawMaterialAssigned() && this.rawMaterial == toAssign) return;
        if (isRawMaterialAssigned() && !isEmpty()) {
            throw new RawMaterialConflictException(warehouseId, this.rawMaterial, toAssign);
        }
        setRawMaterial(toAssign);
    }

    public boolean isRawMaterialAssigned() {
        return rawMaterial != null;
    }

    // The snapshot captures the current Balance
    public void snapshotBalance(final LocalDateTime now) {
        final BigDecimal currentAmount = balance();
        updateSnapshot(new Balance(now, currentAmount));
    }

    private void updateSnapshot(final Balance newSnapshot) {
        this.baseBalance = newSnapshot;
    }

    // Helper method for getting the Balance amount
    public BigDecimal balance() {
        return calculateBalance().amount();
    }

    public SellerId getSellerId() {
        return sellerId;
    }

    public WarehouseId getWarehouseId() {
        return warehouseId;
    }

    public WarehouseNumber getWarehouseNumber() {
        return warehouseNumber;
    }

    public StockLedger getStockLedger() {
        return stockLedger;
    }

    private void setRawMaterial(final RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public Balance getBaseBalance() {
        return baseBalance;
    }

    public SiteLocation getSiteLocation() {
        return siteLocation;
    }

    private BigDecimal calculateMaxOverflowCapacity() {
        return REGULAR_CAPACITY.amount().multiply(MAX_OVERFLOW_CAPACITY_RATIO);
    }

    public List<StoredDeliveryRemainder> getStoredDeliveryRemainders(final LocalDateTime upTo) {
        return stockLedger.getStoredDeliveryRemainders(upTo);
    }

    public List<Delivery> getDeliveries() {
        return stockLedger.getDeliveries();
    }

    public List<Shipment> getShipments() {
        return stockLedger.getShipments();
    }

    public List<ShipmentAllocation> getShipmentAllocations() {
        return stockLedger.getShipmentAllocations();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final Warehouse that = (Warehouse) other;
        return Objects.equals(warehouseId, that.warehouseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseId);
    }
}
