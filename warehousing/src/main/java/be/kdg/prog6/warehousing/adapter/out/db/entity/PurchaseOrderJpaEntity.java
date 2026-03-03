package be.kdg.prog6.warehousing.adapter.out.db.entity;

import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(catalog = "warehousing", name = "purchase_orders")
public class PurchaseOrderJpaEntity {
    @Id
    @Column(name = "purchase_order_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID purchaseOrderId;

    @Column(name = "po_number", length = 20)
    private String poNumber;

    @Column(name = "buyer_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID buyerId;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "seller_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID sellerId;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "vessel_number", nullable = true)
    private String vesselNumber; // Will be filled in after receiving ShippingOrderEntered event from Waterside BC

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineJpaEntity> orderLines;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PurchaseOrderStatus status;

    public UUID getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(final UUID purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(final String poNumber) {
        this.poNumber = poNumber;
    }

    public UUID getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(final UUID buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(final String buyerName) {
        this.buyerName = buyerName;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public void setSellerId(final UUID sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(final String sellerName) {
        this.sellerName = sellerName;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getVesselNumber() {
        return vesselNumber;
    }

    public void setVesselNumber(final String vesselNumber) {
        this.vesselNumber = vesselNumber;
    }

    public List<OrderLineJpaEntity> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(final List<OrderLineJpaEntity> orderLines) {
        this.orderLines = orderLines;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(final PurchaseOrderStatus status) {
        this.status = status;
    }
}
