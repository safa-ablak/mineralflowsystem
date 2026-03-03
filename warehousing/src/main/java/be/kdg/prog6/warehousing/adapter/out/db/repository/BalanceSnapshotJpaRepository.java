package be.kdg.prog6.warehousing.adapter.out.db.repository;

import be.kdg.prog6.warehousing.adapter.out.db.entity.BalanceSnapshotJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BalanceSnapshotJpaRepository extends JpaRepository<BalanceSnapshotJpaEntity, Long> {
    /**
     * Finds all balance snapshots for a given warehouse, ordered by snapshot time (oldest to newest).
     *
     * @param warehouseId the UUID of the warehouse
     * @return list of balance snapshots
     */
    List<BalanceSnapshotJpaEntity> findAllByWarehouseIdOrderBySnapshotTimeAsc(UUID warehouseId);

    /**
     * Finds the most recent balance snapshot for a warehouse at or before the given time.
     *
     * @param warehouseId the UUID of the warehouse
     * @param time        the point in time to query
     * @return the most recent snapshot at or before the given time, if any
     */
    Optional<BalanceSnapshotJpaEntity> findFirstByWarehouseIdAndSnapshotTimeLessThanEqualOrderBySnapshotTimeDesc(
        UUID warehouseId,
        LocalDateTime time
    );
}
