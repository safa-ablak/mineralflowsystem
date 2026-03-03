package be.kdg.prog6.common.event.landside;

import java.util.UUID;

public record TruckDockedEvent(
    UUID warehouseId,
    String dockNumber,
    String rawMaterial
) {
}
