package be.kdg.prog6.waterside.adapter.out.db.repository;

import be.kdg.prog6.waterside.adapter.out.db.entity.InspectionOperationJpaEntity;
import be.kdg.prog6.waterside.domain.InspectionOperationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InspectionOperationJpaRepository extends JpaRepository<InspectionOperationJpaEntity, UUID> {
    List<InspectionOperationJpaEntity> findByStatus(InspectionOperationStatus status);
}
