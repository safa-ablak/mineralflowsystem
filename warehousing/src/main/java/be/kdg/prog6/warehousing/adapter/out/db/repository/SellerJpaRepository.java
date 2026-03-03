package be.kdg.prog6.warehousing.adapter.out.db.repository;

import be.kdg.prog6.warehousing.adapter.out.db.entity.SellerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SellerJpaRepository extends JpaRepository<SellerJpaEntity, UUID> {
    @Query("SELECT s.id FROM SellerJpaEntity s")
    List<UUID> findAllIds();
}