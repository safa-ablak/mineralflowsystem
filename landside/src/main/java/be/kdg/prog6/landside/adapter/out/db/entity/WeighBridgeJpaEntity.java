package be.kdg.prog6.landside.adapter.out.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(catalog = "landside", name = "weigh_bridges")
public class WeighBridgeJpaEntity {
    @Id
    @Column(name = "weigh_bridge_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID weighBridgeId;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Column(name = "occupied_by_visit_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID occupiedByVisitId;

    public WeighBridgeJpaEntity() {
    }

    public WeighBridgeJpaEntity(final UUID weighBridgeId, final String number) {
        this.weighBridgeId = weighBridgeId;
        this.number = number;
        this.occupiedByVisitId = null;
    }

    public UUID getWeighBridgeId() {
        return weighBridgeId;
    }

    public void setWeighBridgeId(final UUID weighBridgeId) {
        this.weighBridgeId = weighBridgeId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public UUID getOccupiedByVisitId() {
        return occupiedByVisitId;
    }

    public void setOccupiedByVisitId(final UUID occupiedByVisitId) {
        this.occupiedByVisitId = occupiedByVisitId;
    }
}
