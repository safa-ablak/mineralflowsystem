package be.kdg.prog6.warehousing.adapter.out.persistence.repository;

import be.kdg.prog6.warehousing.adapter.out.persistence.entity.WarehouseShipmentJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.persistence.entity.WarehouseShipmentJpaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WarehouseShipmentJpaRepository extends JpaRepository<WarehouseShipmentJpaEntity, WarehouseShipmentJpaId> {
    List<WarehouseShipmentJpaEntity> findAllById_WarehouseId(UUID warehouseId);

    List<WarehouseShipmentJpaEntity> findAllById_WarehouseIdAndTimeAfter(UUID warehouseId, LocalDateTime time);

    List<WarehouseShipmentJpaEntity> findAllById_WarehouseIdAndTimeBetween(UUID warehouseId, LocalDateTime from, LocalDateTime to);
}