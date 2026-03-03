package be.kdg.prog6.warehousing.adapter.out.db.repository;

import be.kdg.prog6.warehousing.adapter.out.db.entity.PayloadDeliveryTicketJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PayloadDeliveryTicketJpaRepository extends JpaRepository<PayloadDeliveryTicketJpaEntity, UUID> {
    List<PayloadDeliveryTicketJpaEntity> findByWarehouseId(UUID warehouseId);
}

