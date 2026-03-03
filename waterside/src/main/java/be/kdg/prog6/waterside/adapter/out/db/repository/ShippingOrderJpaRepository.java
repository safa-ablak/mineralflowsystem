package be.kdg.prog6.waterside.adapter.out.db.repository;

import be.kdg.prog6.waterside.adapter.out.db.entity.ShippingOrderJpaEntity;
import be.kdg.prog6.waterside.domain.BunkeringOperationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShippingOrderJpaRepository extends JpaRepository<ShippingOrderJpaEntity, UUID> {
    Optional<ShippingOrderJpaEntity> findByShippingOrderId(UUID shippingOrderId);

    @Query("""
            SELECT so FROM ShippingOrderJpaEntity so
            JOIN so.bunkeringOperation bo
            WHERE bo.status = :status
            ORDER BY bo.queuedAt ASC
        """)
    List<ShippingOrderJpaEntity> findByBunkeringOperationStatusOrderedByQueuedAt(@Param("status") BunkeringOperationStatus status);

    Optional<ShippingOrderJpaEntity> findByReferenceId(UUID referenceId);

    @Query("SELECT so FROM ShippingOrderJpaEntity so WHERE so.actualArrivalDate IS NOT NULL AND so.actualDepartureDate IS NULL")
    List<ShippingOrderJpaEntity> findShippingOrdersOnSite();

    @Query("SELECT COUNT(so) FROM ShippingOrderJpaEntity so WHERE so.actualArrivalDate IS NOT NULL AND so.actualDepartureDate IS NULL")
    int countShippingOrdersOnSite();
}
