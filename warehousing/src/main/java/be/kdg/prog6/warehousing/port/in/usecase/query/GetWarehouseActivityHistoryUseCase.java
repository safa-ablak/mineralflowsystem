package be.kdg.prog6.warehousing.port.in.usecase.query;

import be.kdg.prog6.warehousing.port.in.query.GetWarehouseActivityHistoryQuery;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.WarehouseActivityHistory;

@FunctionalInterface
public interface GetWarehouseActivityHistoryUseCase {
    WarehouseActivityHistory getWarehouseActivityHistory(GetWarehouseActivityHistoryQuery query);
}
