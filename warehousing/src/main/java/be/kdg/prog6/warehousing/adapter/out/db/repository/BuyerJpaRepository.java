package be.kdg.prog6.warehousing.adapter.out.db.repository;

import be.kdg.prog6.warehousing.adapter.out.db.entity.BuyerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BuyerJpaRepository extends JpaRepository<BuyerJpaEntity, UUID> {
}
