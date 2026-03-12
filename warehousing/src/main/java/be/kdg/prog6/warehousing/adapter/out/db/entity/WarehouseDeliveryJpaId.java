package be.kdg.prog6.warehousing.adapter.out.db.entity;

import be.kdg.prog6.warehousing.domain.storage.DeliveryId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class WarehouseDeliveryJpaId implements Serializable {
    @Column(name = "warehouse_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID warehouseId;

    @Column(name = "delivery_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID deliveryId;

    public WarehouseDeliveryJpaId() {}

    public WarehouseDeliveryJpaId(final UUID warehouseId, final UUID deliveryId) {
        this.warehouseId = warehouseId;
        this.deliveryId = deliveryId;
    }

    public static WarehouseDeliveryJpaId of(final DeliveryId deliveryId) {
        return new WarehouseDeliveryJpaId(deliveryId.warehouseId().id(), deliveryId.id());
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public UUID getDeliveryId() {
        return deliveryId;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final WarehouseDeliveryJpaId that = (WarehouseDeliveryJpaId) other;
        return Objects.equals(warehouseId, that.warehouseId)
            && Objects.equals(deliveryId, that.deliveryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseId, deliveryId);
    }
}
