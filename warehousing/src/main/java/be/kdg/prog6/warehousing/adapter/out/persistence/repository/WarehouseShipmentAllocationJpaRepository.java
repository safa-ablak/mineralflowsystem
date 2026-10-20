package be.kdg.prog6.warehousing.adapter.out.persistence.repository;

import be.kdg.prog6.warehousing.adapter.out.persistence.entity.WarehouseShipmentAllocationJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.persistence.entity.WarehouseShipmentAllocationJpaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WarehouseShipmentAllocationJpaRepository
    extends JpaRepository<WarehouseShipmentAllocationJpaEntity, WarehouseShipmentAllocationJpaId> {
    List<WarehouseShipmentAllocationJpaEntity> findAllById_WarehouseId(UUID warehouseId);
}
