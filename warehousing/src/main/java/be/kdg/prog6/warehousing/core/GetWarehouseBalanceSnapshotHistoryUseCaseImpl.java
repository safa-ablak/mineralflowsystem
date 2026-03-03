package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetWarehouseBalanceSnapshotHistoryUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.BalanceSnapshot;
import be.kdg.prog6.warehousing.port.out.BalanceSnapshotPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetWarehouseBalanceSnapshotHistoryUseCaseImpl implements GetWarehouseBalanceSnapshotHistoryUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetWarehouseBalanceSnapshotHistoryUseCaseImpl.class);

    private final BalanceSnapshotPort balanceSnapshotPort;

    public GetWarehouseBalanceSnapshotHistoryUseCaseImpl(final BalanceSnapshotPort balanceSnapshotPort) {
        this.balanceSnapshotPort = balanceSnapshotPort;
    }

    @Override
    public List<BalanceSnapshot> getBalanceSnapshotHistoryFor(final WarehouseId warehouseId) {
        LOGGER.info("Getting Balance Snapshot history for Warehouse with ID {}", warehouseId.id());
        return balanceSnapshotPort.loadBalanceSnapshotHistoryFor(warehouseId);
    }
}
