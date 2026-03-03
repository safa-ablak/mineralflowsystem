package be.kdg.prog6.warehousing.port.in.usecase.query;

import be.kdg.prog6.warehousing.domain.storage.Balance;
import be.kdg.prog6.warehousing.port.in.query.GetWarehouseBalanceQuery;

@FunctionalInterface
public interface GetWarehouseBalanceUseCase {
    Balance getBalance(GetWarehouseBalanceQuery query);
}