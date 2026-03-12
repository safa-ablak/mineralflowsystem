package be.kdg.prog6.landside.adapter.out.db.entity;

import be.kdg.prog6.landside.domain.RawMaterial;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity(name = "LandsideWarehouseJpaEntity")
@Table(catalog = "landside", name = "landside_warehouses")
public class WarehouseJpaEntity {
    @Id
    @Column(name = "warehouse_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID warehouseId;

    @Column(name = "supplier_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID supplierId;

    @Column(name = "raw_material", nullable = true)
    @Enumerated(value = EnumType.STRING)
    private RawMaterial rawMaterial;

    @Column(name = "is_available")
    private boolean available;

    public WarehouseJpaEntity() {}

    public WarehouseJpaEntity(final UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(final UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(final UUID supplierId) {
        this.supplierId = supplierId;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(final RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(final boolean available) {
        this.available = available;
    }
}
