package be.kdg.prog6.warehousing.adapter.out.db.entity;

import be.kdg.prog6.warehousing.domain.storage.ShipmentId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class WarehouseShipmentJpaId implements Serializable {
    @Column(name = "warehouse_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID warehouseId;

    @Column(name = "shipment_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID shipmentId;

    public WarehouseShipmentJpaId() {}

    public WarehouseShipmentJpaId(final UUID warehouseId, final UUID shipmentId) {
        this.warehouseId = warehouseId;
        this.shipmentId = shipmentId;
    }

    public static WarehouseShipmentJpaId of(final ShipmentId shipmentId) {
        return new WarehouseShipmentJpaId(shipmentId.warehouseId().id(), shipmentId.id());
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final WarehouseShipmentJpaId that = (WarehouseShipmentJpaId) other;
        return Objects.equals(warehouseId, that.warehouseId)
            && Objects.equals(shipmentId, that.shipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseId, shipmentId);
    }
}
