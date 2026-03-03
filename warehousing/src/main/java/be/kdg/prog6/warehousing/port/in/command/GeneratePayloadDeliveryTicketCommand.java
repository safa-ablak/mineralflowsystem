package be.kdg.prog6.warehousing.port.in.command;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import static java.util.Objects.requireNonNull;

public record GeneratePayloadDeliveryTicketCommand(
    WarehouseId warehouseId,
    String dockNumber
) {
    public GeneratePayloadDeliveryTicketCommand {
        requireNonNull(warehouseId);
        requireNonNull(dockNumber);
        if (dockNumber.isBlank()) {
            throw new InvalidOperationException("Dock number must not be blank");
        }
    }
}
