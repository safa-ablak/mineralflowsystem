package be.kdg.prog6.landside.adapter.out.db.repository;

import be.kdg.prog6.landside.adapter.out.db.entity.WarehouseJpaEntity;
import be.kdg.prog6.landside.domain.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("landsideWarehouseRepository")
public interface WarehouseJpaRepository extends JpaRepository<WarehouseJpaEntity, UUID> {
    Optional<WarehouseJpaEntity> findByWarehouseId(UUID warehouseId);

    Optional<WarehouseJpaEntity> findBySupplierIdAndRawMaterial(UUID supplierId, RawMaterial rawMaterial);
}
