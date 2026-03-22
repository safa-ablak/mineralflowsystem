package be.kdg.prog6.common.event.warehousing;

import java.util.UUID;

/**
 * Published when a warehouse's assigned raw material changes.
 *
 * <p><b>Example:</b> A warehouse initially storing GYPSUM is emptied after a shipment.
 * Since it is now empty, it can be reassigned to a different raw material (e.g. IRON_ORE).
 *
 * <p>When the Landside BC receives this event, any scheduled appointments targeting this warehouse
 * whose raw material no longer matches are automatically cancelled.
 */
public record WarehouseRawMaterialAssignedEvent(
    UUID warehouseId,
    String rawMaterial
) {}