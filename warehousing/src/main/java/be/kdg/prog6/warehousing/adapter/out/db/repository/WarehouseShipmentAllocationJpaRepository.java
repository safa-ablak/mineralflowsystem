package be.kdg.prog6.warehousing.adapter.out.db.repository;

import be.kdg.prog6.warehousing.adapter.out.db.entity.WarehouseShipmentAllocationJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.db.entity.WarehouseShipmentAllocationJpaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WarehouseShipmentAllocationJpaRepository extends JpaRepository<WarehouseShipmentAllocationJpaEntity, WarehouseShipmentAllocationJpaId> {
    List<WarehouseShipmentAllocationJpaEntity> findAllById_WarehouseId(UUID warehouseId);
}
