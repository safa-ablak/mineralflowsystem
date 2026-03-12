package be.kdg.prog6.warehousing.adapter.out.db.entity;

import be.kdg.prog6.warehousing.adapter.out.db.value.SiteLocationEmbeddable;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "warehousing", name = "warehouses")
public class WarehouseJpaEntity {
    @Id
    @Column(name = "warehouse_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID warehouseId;

    @Column(name = "seller_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID sellerId;

    @Column(name = "warehouse_number", nullable = false, unique = true)
    private String warehouseNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "raw_material", nullable = true)
    private RawMaterial rawMaterial;

    @Column(name = "percentage_filled", precision = 12, scale = 4, nullable = false)
    private BigDecimal percentageFilled; // in %

    @Column(name = "balance", precision = 12, scale = 4, nullable = false)
    private BigDecimal balance; // in tons

    @Column(name = "balance_updated_at", nullable = false)
    private LocalDateTime balanceUpdatedAt;

    /* - Deliveries and shipments could be mapped here as:
    *   @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    *   private List<WarehouseDeliveryJpaEntity> deliveries;
    *
    *   @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    *   private List<WarehouseShipmentJpaEntity> shipments;
    *
    *   but they are queried via their own repositories with date filters instead,
    *   to avoid loading an unbounded, ever-growing collection
    */

    @Embedded
    private SiteLocationEmbeddable siteLocation;

    public WarehouseJpaEntity() {}

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(final UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public void setSellerId(final UUID sellerId) {
        this.sellerId = sellerId;
    }

    public String getWarehouseNumber() {
        return warehouseNumber;
    }

    public void setWarehouseNumber(final String warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(final RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getPercentageFilled() {
        return percentageFilled;
    }

    public void setPercentageFilled(final BigDecimal percentageFilled) {
        this.percentageFilled = percentageFilled;
    }

    public LocalDateTime getBalanceUpdatedAt() {
        return balanceUpdatedAt;
    }

    public void setBalanceUpdatedAt(final LocalDateTime balanceUpdatedAt) {
        this.balanceUpdatedAt = balanceUpdatedAt;
    }

    public SiteLocationEmbeddable getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(final SiteLocationEmbeddable siteLocation) {
        this.siteLocation = siteLocation;
    }
}
