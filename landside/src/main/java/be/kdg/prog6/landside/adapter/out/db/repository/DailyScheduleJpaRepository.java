package be.kdg.prog6.landside.adapter.out.db.repository;

import be.kdg.prog6.landside.adapter.out.db.entity.DailyScheduleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyScheduleJpaRepository
    extends JpaRepository<DailyScheduleJpaEntity, LocalDate> {
}
