package be.kdg.prog6.invoicing.adapter.out.db.repository;

import be.kdg.prog6.invoicing.adapter.out.db.entity.RawMaterialJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RawMaterialJpaRepository extends JpaRepository<RawMaterialJpaEntity, UUID> {
    Optional<RawMaterialJpaEntity> findByNameIgnoreCase(String name);
}
