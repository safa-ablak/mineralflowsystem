package be.kdg.prog6.warehousing.adapter.out.db.adapter;

import be.kdg.prog6.warehousing.adapter.out.db.entity.BalanceSnapshotJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.db.repository.BalanceSnapshotJpaRepository;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.BalanceSnapshot;
import be.kdg.prog6.warehousing.port.out.BalanceSnapshotPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Infrastructure adapter for persisting and loading balance snapshots using JPA.
 * Implements the {@link BalanceSnapshotPort} from the application (port.out) layer.
 */
@Component
public class BalanceSnapshotDatabaseAdapter implements BalanceSnapshotPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceSnapshotDatabaseAdapter.class);

    private final BalanceSnapshotJpaRepository balanceSnapshotJpaRepository;

    public BalanceSnapshotDatabaseAdapter(final BalanceSnapshotJpaRepository balanceSnapshotJpaRepository) {
        this.balanceSnapshotJpaRepository = balanceSnapshotJpaRepository;
    }

    @Override
    public List<BalanceSnapshot> loadBalanceSnapshotHistoryFor(final WarehouseId warehouseId) {
        LOGGER.info("Loading Balance Snapshot history for Warehouse with ID {}", warehouseId.id());
        return balanceSnapshotJpaRepository
            .findAllByWarehouseIdOrderBySnapshotTimeAsc(warehouseId.id())
            .stream()
            .map(this::toBalanceSnapshot)
            .toList();
    }

    @Override
    public Optional<BalanceSnapshot> loadMostRecentSnapshotAsOf(final WarehouseId warehouseId,
                                                                final LocalDateTime asOf) {
        LOGGER.info("Loading most recent Balance Snapshot for Warehouse {} as of {}", warehouseId.id(), asOf);
        return balanceSnapshotJpaRepository
            .findFirstByWarehouseIdAndSnapshotTimeLessThanEqualOrderBySnapshotTimeDesc(warehouseId.id(), asOf)
            .map(this::toBalanceSnapshot);
    }

    private BalanceSnapshot toBalanceSnapshot(final BalanceSnapshotJpaEntity balanceSnapshotJpaEntity) {
        return new BalanceSnapshot(
            WarehouseId.of(balanceSnapshotJpaEntity.getWarehouseId()),
            balanceSnapshotJpaEntity.getSnapshotTime(),
            balanceSnapshotJpaEntity.getBalance()
        );
    }

    @Override
    public void saveBalanceSnapshot(final BalanceSnapshot balanceSnapshot) {
        final BalanceSnapshotJpaEntity balanceSnapshotJpaEntity = new BalanceSnapshotJpaEntity();
        balanceSnapshotJpaEntity.setWarehouseId(balanceSnapshot.warehouseId().id());
        balanceSnapshotJpaEntity.setSnapshotTime(balanceSnapshot.snapshotTime());
        balanceSnapshotJpaEntity.setBalance(balanceSnapshot.amount());
        balanceSnapshotJpaRepository.save(balanceSnapshotJpaEntity);
    }
}
