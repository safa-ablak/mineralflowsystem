package be.kdg.prog6.invoicing.adapter.out.db.entity;

import be.kdg.prog6.invoicing.adapter.out.db.value.MonetaryValueEmbeddable;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(catalog = "invoicing", name = "raw_materials")
public class RawMaterialJpaEntity {
    @Id
    @Column(name = "id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "storage_price_amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "storage_price_currency", nullable = false, length = 3))
    })
    private MonetaryValueEmbeddable storagePrice;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "unit_price_amount", nullable = false)),
        @AttributeOverride(name = "currency", column = @Column(name = "unit_price_currency", nullable = false, length = 3))
    })
    private MonetaryValueEmbeddable unitPrice;

    public RawMaterialJpaEntity() {
    }

    public RawMaterialJpaEntity(final UUID id, final String name, final MonetaryValueEmbeddable storagePrice, final MonetaryValueEmbeddable unitPrice) {
        this.id = id;
        this.name = name;
        this.storagePrice = storagePrice;
        this.unitPrice = unitPrice;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public MonetaryValueEmbeddable getStoragePrice() {
        return storagePrice;
    }

    public void setStoragePrice(final MonetaryValueEmbeddable storagePrice) {
        this.storagePrice = storagePrice;
    }

    public MonetaryValueEmbeddable getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(final MonetaryValueEmbeddable unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final RawMaterialJpaEntity that = (RawMaterialJpaEntity) other;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
