package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.exception.storage.WarehouseNotFoundException;
import be.kdg.prog6.warehousing.domain.storage.Balance;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.query.GetWarehouseBalanceQuery;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetWarehouseBalanceUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.BalanceSnapshot;
import be.kdg.prog6.warehousing.port.out.BalanceSnapshotPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class GetWarehouseBalanceUseCaseImpl implements GetWarehouseBalanceUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetWarehouseBalanceUseCaseImpl.class);

    private final LoadWarehousePort loadWarehousePort;
    private final BalanceSnapshotPort balanceSnapshotPort;

    public GetWarehouseBalanceUseCaseImpl(final LoadWarehousePort loadWarehousePort,
                                          final BalanceSnapshotPort balanceSnapshotPort) {
        this.loadWarehousePort = loadWarehousePort;
        this.balanceSnapshotPort = balanceSnapshotPort;
    }

    @Override
    public Balance getBalance(final GetWarehouseBalanceQuery query) {
        final WarehouseId warehouseId = query.warehouseId();
        final LocalDateTime asOf = query.asOf();
        LOGGER.info("Getting Balance for Warehouse {} as of {}", warehouseId.id(), asOf);
        /* Efficient way to calculate Balance -> Load the latest snapshot before 'asOf' for Warehouse to reduce event replays */
        final BalanceSnapshot latestSnapshot = balanceSnapshotPort.loadMostRecentSnapshotAsOf(warehouseId, asOf)
            .orElseGet(() -> BalanceSnapshot.origin(warehouseId));

        final Warehouse warehouse = loadWarehousePort.loadWarehouseByIdWithActivitiesBetween(
            warehouseId,
            latestSnapshot.snapshotTime(), asOf
        ).orElseThrow(() -> WarehouseNotFoundException.forId(warehouseId));
//        LoggingUtils.logWarehouseActivitiesIfDebug(warehouse); // Would only log activities after the latest snapshot
        final BigDecimal netChangeSinceLatestSnapshot = warehouse.balance();
        return new Balance(asOf, latestSnapshot.amount().add(netChangeSinceLatestSnapshot));
    }
}
