package be.kdg.prog6.warehousing.domain.storage;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PDT: Official payload delivery ticket:<br>
 * - type of material<br>
 * - generation time (when we issued the ticket)<br>
 * - warehouseNumber<br>
 * - dockNumber<br>
 */
public record PayloadDeliveryTicket(
    PayloadDeliveryTicketId id,
    String rawMaterial,
    LocalDateTime generationTime,
    WarehouseId warehouseId,
    String warehouseNumber,
    String dockNumber) {
    public PayloadDeliveryTicket(final String rawMaterial,
                                 final WarehouseId warehouseId,
                                 final String warehouseNumber,
                                 final String dockNumber) {
        this(PayloadDeliveryTicketId.of(UUID.randomUUID()), rawMaterial, LocalDateTime.now(), warehouseId, warehouseNumber, dockNumber);
    }
}