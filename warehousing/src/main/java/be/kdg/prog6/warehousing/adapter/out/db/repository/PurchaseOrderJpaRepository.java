package be.kdg.prog6.warehousing.adapter.out.db.repository;

import be.kdg.prog6.warehousing.adapter.out.db.entity.PurchaseOrderJpaEntity;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderJpaRepository extends JpaRepository<PurchaseOrderJpaEntity, UUID> {
    Optional<PurchaseOrderJpaEntity> findByBuyerIdAndPurchaseOrderIdAndStatus(
        UUID buyerId,
        UUID purchaseOrderId,
        PurchaseOrderStatus status
    );

    @Query("""
            SELECT po FROM PurchaseOrderJpaEntity po
            WHERE (:status IS NULL OR po.status = :status)
            AND (:sellerName IS NULL OR LOWER(po.sellerName) LIKE LOWER(CONCAT('%', :sellerName, '%')))
        """)
    List<PurchaseOrderJpaEntity> findByStatusAndSellerName(
        @Param("status") PurchaseOrderStatus status,
        @Param("sellerName") String sellerName
    );
}
