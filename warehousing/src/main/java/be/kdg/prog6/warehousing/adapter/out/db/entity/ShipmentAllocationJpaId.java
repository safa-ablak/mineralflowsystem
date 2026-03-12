package be.kdg.prog6.warehousing.adapter.out.db.entity;

import be.kdg.prog6.warehousing.domain.storage.DeliveryId;
import be.kdg.prog6.warehousing.domain.storage.ShipmentId;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class ShipmentAllocationJpaId implements Serializable {
    @Column(name = "warehouse_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID warehouseId;

    @Column(name = "shipment_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID shipmentId;

    @Column(name = "delivery_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID deliveryId;

    public ShipmentAllocationJpaId() {}

    public ShipmentAllocationJpaId(final UUID warehouseId, final UUID shipmentId, final UUID deliveryId) {
        this.warehouseId = warehouseId;
        this.shipmentId = shipmentId;
        this.deliveryId = deliveryId;
    }

    public static ShipmentAllocationJpaId of(final WarehouseId warehouseId, final ShipmentId shipmentId, final DeliveryId deliveryId) {
        return new ShipmentAllocationJpaId(warehouseId.id(), shipmentId.id(), deliveryId.id());
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public UUID getShipmentId() {
        return shipmentId;
    }

    public UUID getDeliveryId() {
        return deliveryId;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final ShipmentAllocationJpaId that = (ShipmentAllocationJpaId) other;
        return Objects.equals(warehouseId, that.warehouseId)
            && Objects.equals(shipmentId, that.shipmentId)
            && Objects.equals(deliveryId, that.deliveryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseId, shipmentId, deliveryId);
    }
}
