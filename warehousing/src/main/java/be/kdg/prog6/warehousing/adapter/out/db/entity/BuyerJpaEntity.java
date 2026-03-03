package be.kdg.prog6.warehousing.adapter.out.db.entity;

import be.kdg.prog6.warehousing.adapter.out.db.value.AddressEmbeddable;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(catalog = "warehousing", name = "buyers")
public class BuyerJpaEntity {
    @Id
    @Column(name = "id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private AddressEmbeddable address;

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

    public AddressEmbeddable getAddress() {
        return address;
    }

    public void setAddress(final AddressEmbeddable address) {
        this.address = address;
    }
}
