package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.BalanceSnapshot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BalanceSnapshotPort {
    List<BalanceSnapshot> loadBalanceSnapshotHistoryFor(WarehouseId warehouseId);

    void saveBalanceSnapshot(BalanceSnapshot balanceSnapshot);

    /**
     * Loads the most recent balance snapshot for a warehouse at or before the given time.
     *
     * @param warehouseId the warehouse to query
     * @param asOf        the point in time
     * @return the snapshot if found, empty otherwise
     */
    Optional<BalanceSnapshot> loadMostRecentSnapshotAsOf(WarehouseId warehouseId, LocalDateTime asOf);
}
