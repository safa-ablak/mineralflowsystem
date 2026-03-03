package be.kdg.prog6.warehousing.port.in.usecase.query;

import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.WarehouseOverview;

@FunctionalInterface
public interface GetWarehouseOverviewUseCase {
    WarehouseOverview getWarehouseOverview(WarehouseId warehouseId);
}
