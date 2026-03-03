package be.kdg.prog6.invoicing.adapter.out.db.repository;

import be.kdg.prog6.invoicing.adapter.out.db.entity.InvoiceJpaEntity;
import be.kdg.prog6.invoicing.domain.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceJpaEntity, UUID> {
    Optional<InvoiceJpaEntity> findByCustomerIdAndStatus(UUID customerId, InvoiceStatus status);

    List<InvoiceJpaEntity> findByStatus(InvoiceStatus status);
}
