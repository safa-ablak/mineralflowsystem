package be.kdg.prog6.warehousing.port.in.usecase.query;

import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.BalanceSnapshot;

import java.util.List;

@FunctionalInterface
public interface GetWarehouseBalanceSnapshotHistoryUseCase {
    /**
     * Returns all recorded balance snapshots for the given warehouse, sorted chronologically.
     *
     * @param warehouseId the ID of the warehouse to retrieve snapshots for
     * @return list of historical balance snapshots
     */
    List<BalanceSnapshot> getBalanceSnapshotHistoryFor(WarehouseId warehouseId);
}
