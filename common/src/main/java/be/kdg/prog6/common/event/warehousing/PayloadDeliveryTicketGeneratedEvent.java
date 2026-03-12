package be.kdg.prog6.common.event.warehousing;

import java.time.LocalDateTime;
import java.util.UUID;

public record PayloadDeliveryTicketGeneratedEvent(
    UUID ticketId,
    String warehouseNumber,
    String rawMaterial,
    LocalDateTime generationTime,
    String dockNumber
) {}
