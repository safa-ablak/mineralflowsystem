package be.kdg.prog6.warehousing.adapter.out.persistence.repository;

import be.kdg.prog6.warehousing.adapter.out.persistence.entity.WarehouseDeliveryJpaEntity;
import be.kdg.prog6.warehousing.adapter.out.persistence.entity.WarehouseDeliveryJpaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WarehouseDeliveryJpaRepository extends JpaRepository<WarehouseDeliveryJpaEntity, WarehouseDeliveryJpaId> {
    List<WarehouseDeliveryJpaEntity> findAllById_WarehouseId(UUID warehouseId);

    List<WarehouseDeliveryJpaEntity> findAllById_WarehouseIdAndTimeAfter(UUID warehouseId, LocalDateTime time);

    List<WarehouseDeliveryJpaEntity> findAllById_WarehouseIdAndTimeBetween(UUID warehouseId, LocalDateTime from, LocalDateTime to);
}

