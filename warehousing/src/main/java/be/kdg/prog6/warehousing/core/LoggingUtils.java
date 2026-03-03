package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.query.GetWarehouseActivityHistoryQuery.ViewMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Internal utility for debugging warehouse activity logs.
 * <p>
 * Intended for use only within the core package of the Warehousing Bounded Context.
 */
final class LoggingUtils {
    private LoggingUtils() {
        throw new AssertionError("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingUtils.class);

    /**
     * Logs warehouse activities without allocations (backward compatible).
     * Delegates to {@link #logWarehouseActivitiesIfDebug(Warehouse, ViewMode)} with WITHOUT_ALLOCATIONS mode.
     */
    static void logWarehouseActivitiesIfDebug(final Warehouse warehouse) {
        logWarehouseActivitiesIfDebug(warehouse, ViewMode.WITHOUT_ALLOCATIONS);
    }

    /**
     * Logs warehouse activities based on the specified view mode.
     * When view mode is WITH_ALLOCATIONS, also logs shipment allocations.
     */
    static void logWarehouseActivitiesIfDebug(final Warehouse warehouse, final ViewMode viewMode) {
        if (!LOGGER.isDebugEnabled()) return;

        final WarehouseId id = warehouse.getWarehouseId();
        final String deliveries = formatCollection(warehouse.getDeliveries(), "no deliveries");
        final String shipments = formatCollection(warehouse.getShipments(), "no shipments");
        switch (viewMode) {
            case WITHOUT_ALLOCATIONS -> LOGGER.debug("""
                Activities loaded for Warehouse with ID {}:
                Deliveries:
                {}
                Shipments:
                {}
                """, id.id(), deliveries, shipments);
            case WITH_ALLOCATIONS -> {
                final String allocations = formatCollection(warehouse.getShipmentAllocations(), "no allocations");
                LOGGER.debug("""
                    Activities loaded for Warehouse with ID {} (with allocations):
                    Deliveries:
                    {}
                    Shipments:
                    {}
                    Shipment Allocations:
                    {}
                    """, id.id(), deliveries, shipments, allocations);
            }
        }
    }

    private static <T> String formatCollection(final Collection<T> items, final String emptyMessage) {
        return items.isEmpty()
            ? "<" + emptyMessage + ">"
            : items.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }
}
