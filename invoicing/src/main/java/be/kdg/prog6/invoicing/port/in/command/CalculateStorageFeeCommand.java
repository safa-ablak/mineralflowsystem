package be.kdg.prog6.invoicing.port.in.command;

import be.kdg.prog6.common.event.warehousing.WarehouseStorageReport;
import be.kdg.prog6.invoicing.domain.CustomerId;

import java.util.List;

import static java.util.Objects.requireNonNull;

public record CalculateStorageFeeCommand(
    CustomerId customerId,
    List<WarehouseStorageReport> warehouseStorageReports
) {
    public CalculateStorageFeeCommand {
        requireNonNull(customerId);
        requireNonNull(warehouseStorageReports);
    }
}
