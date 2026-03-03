package be.kdg.prog6.warehousing.adapter.out.db.repository;

import be.kdg.prog6.warehousing.adapter.out.db.entity.ShipmentAllocationJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.db.entity.ShipmentAllocationJpaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShipmentAllocationJpaRepository extends JpaRepository<ShipmentAllocationJpaEntity, ShipmentAllocationJpaId> {
    List<ShipmentAllocationJpaEntity> findAllById_WarehouseId(UUID warehouseId);
}
