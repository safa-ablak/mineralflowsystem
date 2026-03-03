package be.kdg.prog6.warehousing.port.in.usecase.query;

import be.kdg.prog6.warehousing.port.in.query.CalculateWarehouseBalanceChangeQuery;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.NetBalanceChange;

@FunctionalInterface
public interface CalculateWarehouseBalanceChangeUseCase {
    NetBalanceChange calculateNetBalanceChange(CalculateWarehouseBalanceChangeQuery query);
}
