package be.kdg.prog6.landside.adapter.out.db.repository;

import be.kdg.prog6.landside.adapter.out.db.entity.AppointmentJpaEntity;
import be.kdg.prog6.landside.domain.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentJpaEntity, UUID> {
    List<AppointmentJpaEntity> findByArrivalWindowStartBetweenOrderByArrivalWindowStart(
        LocalDateTime from, LocalDateTime to
    );

    List<AppointmentJpaEntity> findByWarehouseIdAndStatus(UUID warehouseId, AppointmentStatus status);
}
