package be.kdg.prog6.warehousing.port.in.query;

import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import static java.util.Objects.requireNonNull;

public record GetWarehouseActivityHistoryQuery(
    WarehouseId warehouseId,
    ViewMode viewMode
) {
    public GetWarehouseActivityHistoryQuery {
        requireNonNull(warehouseId);
        requireNonNull(viewMode);
    }

    public GetWarehouseActivityHistoryQuery(final WarehouseId warehouseId) {
        this(warehouseId, ViewMode.WITHOUT_ALLOCATIONS);
    }

    public enum ViewMode {
        WITHOUT_ALLOCATIONS,
        WITH_ALLOCATIONS
    }
}
