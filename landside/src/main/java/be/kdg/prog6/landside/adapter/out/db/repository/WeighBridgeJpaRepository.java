package be.kdg.prog6.landside.adapter.out.db.repository;

import be.kdg.prog6.landside.adapter.out.db.entity.WeighBridgeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WeighBridgeJpaRepository extends JpaRepository<WeighBridgeJpaEntity, UUID> {
    @Query("SELECT wb FROM WeighBridgeJpaEntity wb WHERE wb.occupiedByVisitId IS NULL ORDER BY wb.number ASC LIMIT 1")
    Optional<WeighBridgeJpaEntity> findFirstAvailable();

    Optional<WeighBridgeJpaEntity> findByOccupiedByVisitId(UUID visitId);
}
