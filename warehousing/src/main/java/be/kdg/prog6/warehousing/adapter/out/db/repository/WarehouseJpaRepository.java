package be.kdg.prog6.warehousing.adapter.out.db.repository;

import be.kdg.prog6.warehousing.adapter.out.db.entity.WarehouseJpaEntity;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WarehouseJpaRepository extends JpaRepository<WarehouseJpaEntity, UUID> {
    List<WarehouseJpaEntity> findBySellerId(UUID sellerId);

    List<WarehouseJpaEntity> findByRawMaterial(RawMaterial rawMaterial);
}
