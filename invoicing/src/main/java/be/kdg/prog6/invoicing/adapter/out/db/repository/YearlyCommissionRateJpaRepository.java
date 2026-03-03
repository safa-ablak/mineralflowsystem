package be.kdg.prog6.invoicing.adapter.out.db.repository;

import be.kdg.prog6.invoicing.adapter.out.db.entity.YearlyCommissionRateJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YearlyCommissionRateJpaRepository
    extends JpaRepository<YearlyCommissionRateJpaEntity, Integer> {
}