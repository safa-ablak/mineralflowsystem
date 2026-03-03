package be.kdg.prog6.warehousing.domain.storage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

/**
 * Represents an append-only ledger of all delivery and shipment events in a warehouse.
 * <p>
 * Used to reconstruct current stock levels and to support FIFO shipment logic from shippable
 * Deliveries with the help of ShipmentAllocations.
 * <p>
 * Didn't name it ActivityWindow since it also contains ShipmentAllocations besides Activities
 * (Deliveries and Shipments), so the name doesn't fully fit here unlike the PiggyBank example
 * where there are only Money PUT_IN and TAKE_OUT Activities.
 */
public class StockLedger {
    private final WarehouseId warehouseId; // For which warehouse this stock ledger is for.
    private final List<Delivery> deliveries; // All recorded deliveries.
    private final List<Shipment> shipments; // All recorded shipments.
    private final List<ShipmentAllocation> shipmentAllocations; // All shipment allocations.

    public StockLedger(final WarehouseId warehouseId,
                       final List<Delivery> deliveries,
                       final List<Shipment> shipments,
                       final List<ShipmentAllocation> shipmentAllocations) {
        this.warehouseId = warehouseId;
        this.deliveries = deliveries;
        this.shipments = shipments;
        this.shipmentAllocations = shipmentAllocations;
    }

    /**
     * Factory method to create an empty StockLedger for a new Warehouse.
     * <p>
     * Keeps the creation logic for a “fresh” ledger in one place and avoids
     * sprinkling 'new ArrayList<>()' everywhere.
     */
    public static StockLedger emptyFor(final WarehouseId warehouseId) {
        return new StockLedger(warehouseId, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Calculates the current balance by subtracting all shipped amounts from delivered amounts.
     *
     * @return Updated balance with net changes applied.
     */
    Balance calculateBalance(final Balance baseBalance) {
        final LocalDateTime baseTime = baseBalance.time();
        // 1. Filter the new deliveries (time > base balance time)
        final BigDecimal totalDelivered = deliveries.stream()
            .filter(delivery -> delivery.time().isAfter(baseTime))
            .map(Delivery::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 2. Filter the new shipments (time > base balance time)
        final BigDecimal totalShipped = shipments.stream()
            .filter(shipment -> shipment.time().isAfter(baseTime))
            .map(Shipment::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 3. Calculate the net change and add it to the base balance
        final BigDecimal netChange = totalDelivered.subtract(totalShipped);
        // 4. Get the latest change time from all deliveries and shipments
        final LocalDateTime latestChangeTime = determineLatestChangeTime(deliveries, shipments, baseTime);
        // 5. Return the updated balance
        return new Balance(latestChangeTime, baseBalance.amount().add(netChange));
    }

    // Helper method to get the latest change time from all deliveries and shipments.
    private static LocalDateTime determineLatestChangeTime(final List<Delivery> deliveries,
                                                           final List<Shipment> shipments,
                                                           final LocalDateTime fallback) {
        return Stream.concat(
                deliveries.stream().map(Delivery::time),
                shipments.stream().map(Shipment::time)
            )
            .max(LocalDateTime::compareTo)
            .orElse(fallback);
    }

    Delivery recordDelivery(final BigDecimal amount) {
        final DeliveryId deliveryId = DeliveryId.forWarehouse(warehouseId);
        final Delivery delivery = new Delivery(
            deliveryId,
            LocalDateTime.now(),
            amount
        );
        deliveries.add(delivery);
        return delivery;
    }

    ShipmentRecord recordShipment(final BigDecimal amount) {
        final ShipmentId shipmentId = ShipmentId.forWarehouse(warehouseId);
        final List<ShipmentAllocation> allocations = allocateFromDeliveriesForShipment(amount, shipmentId);
        final Shipment shipment = new Shipment(
            shipmentId,
            LocalDateTime.now(),
            amount
        );
        shipments.add(shipment);
        shipmentAllocations.addAll(allocations);
        return new ShipmentRecord(shipment, allocations);
    }

    private List<ShipmentAllocation> allocateFromDeliveriesForShipment(final BigDecimal amountToShip, final ShipmentId shipmentId) {
        BigDecimal remaining = amountToShip;
        final List<ShipmentAllocation> allocations = new ArrayList<>();
        final Map<DeliveryId, BigDecimal> allocatedAmounts = getAllocatedAmountsPerDelivery();

        for (Delivery delivery : getShippableDeliveriesSortedByOldest()) { // FIFO
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

            final BigDecimal allocated = allocatedAmounts.getOrDefault(delivery.id(), BigDecimal.ZERO);
            final BigDecimal available = delivery.amount().subtract(allocated);
            final BigDecimal toAllocate = remaining.min(available);

            allocations.add(new ShipmentAllocation(warehouseId, shipmentId, delivery.id(), toAllocate));
            remaining = remaining.subtract(toAllocate);
        }
        return Collections.unmodifiableList(allocations);
    }

    List<StoredDeliveryRemainder> getStoredDeliveryRemainders(final LocalDateTime upTo) {
//        final LocalDateTime now = LocalDateTime.now();
        final List<StoredDeliveryRemainder> storedDeliveryRemainders = new ArrayList<>();
        final Map<DeliveryId, BigDecimal> allocatedAmounts = getAllocatedAmountsPerDelivery();

        for (Delivery delivery : getShippableDeliveries()) { // No need to sort for reporting
            if (delivery.time().isAfter(upTo)) continue; // Skip future deliveries

            final BigDecimal allocated = allocatedAmounts.getOrDefault(delivery.id(), BigDecimal.ZERO);
            final BigDecimal remaining = delivery.amount().subtract(allocated);

            final int daysStored = (int) ChronoUnit.DAYS.between(delivery.time(), upTo);
            storedDeliveryRemainders.add(
                new StoredDeliveryRemainder(delivery.id(), remaining, daysStored)
            );
        }
        return Collections.unmodifiableList(storedDeliveryRemainders);
    }

    List<Delivery> getShippableDeliveries() {
        return findShippableDeliveries(false);
    }

    List<Delivery> getShippableDeliveriesSortedByOldest() {
        return findShippableDeliveries(true);
    }

    private List<Delivery> findShippableDeliveries(final boolean sortByOldest) {
        final Map<DeliveryId, BigDecimal> allocatedAmounts = getAllocatedAmountsPerDelivery();
        final List<Delivery> shippableDeliveries = new ArrayList<>();

        for (Delivery delivery : deliveries) {
            final BigDecimal allocated = allocatedAmounts.getOrDefault(delivery.id(), BigDecimal.ZERO);
            final BigDecimal remaining = delivery.amount().subtract(allocated);
            if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                shippableDeliveries.add(delivery);
            }
        }
        if (sortByOldest) {
            shippableDeliveries.sort(Comparator.comparing(Delivery::time)); // FIFO: oldest first
        }
        return shippableDeliveries;
    }

    private Map<DeliveryId, BigDecimal> getAllocatedAmountsPerDelivery() {
        final Map<DeliveryId, BigDecimal> allocatedAmounts = new HashMap<>();
        for (ShipmentAllocation allocation : this.shipmentAllocations) {
            allocatedAmounts.merge(allocation.deliveryId(), allocation.amountAllocated(), BigDecimal::add);
        }
        return allocatedAmounts;
    }

    List<Delivery> getDeliveries() {
        return Collections.unmodifiableList(deliveries);
    }

    List<Shipment> getShipments() {
        return Collections.unmodifiableList(shipments);
    }

    List<ShipmentAllocation> getShipmentAllocations() {
        return Collections.unmodifiableList(shipmentAllocations);
    }
}
