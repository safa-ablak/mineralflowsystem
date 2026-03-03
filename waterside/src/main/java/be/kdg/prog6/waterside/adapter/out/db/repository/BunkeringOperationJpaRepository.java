package be.kdg.prog6.waterside.adapter.out.db.repository;

import be.kdg.prog6.waterside.adapter.out.db.entity.BunkeringOperationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BunkeringOperationJpaRepository extends JpaRepository<BunkeringOperationJpaEntity, UUID> {
    @Query("SELECT COUNT(bo) FROM BunkeringOperationJpaEntity bo WHERE bo.performedAt >= :start AND bo.performedAt < :end")
    long countPerformedBetween(LocalDateTime start, LocalDateTime end);
}
