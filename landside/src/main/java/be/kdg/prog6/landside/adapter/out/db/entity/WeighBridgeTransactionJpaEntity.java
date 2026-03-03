package be.kdg.prog6.landside.adapter.out.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "landside", name = "weigh_bridge_transactions")
public class WeighBridgeTransactionJpaEntity {
    @Id
    @Column(name = "transaction_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID transactionId;

    @Column(name = "truck_license_plate", nullable = false)
    private String truckLicensePlate;

    @Column(name = "gross_weight", nullable = false)
    private BigDecimal grossWeight;

    @Column(name = "tare_weight", nullable = true)
    private BigDecimal tareWeight;

    @Column(name = "weigh_in_time", nullable = true)
    private LocalDateTime weighInTime;

    @Column(name = "weigh_out_time", nullable = true)
    private LocalDateTime weighOutTime;

    public WeighBridgeTransactionJpaEntity() {
    }

    public WeighBridgeTransactionJpaEntity(final UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getTruckLicensePlate() {
        return truckLicensePlate;
    }

    public void setTruckLicensePlate(final String truckLicensePlate) {
        this.truckLicensePlate = truckLicensePlate;
    }

    public BigDecimal getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(final BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
    }

    public BigDecimal getTareWeight() {
        return tareWeight;
    }

    public void setTareWeight(final BigDecimal tareWeight) {
        this.tareWeight = tareWeight;
    }

    public LocalDateTime getWeighInTime() {
        return weighInTime;
    }

    public void setWeighInTime(final LocalDateTime weighInTime) {
        this.weighInTime = weighInTime;
    }

    public LocalDateTime getWeighOutTime() {
        return weighOutTime;
    }

    public void setWeighOutTime(final LocalDateTime weighOutTime) {
        this.weighOutTime = weighOutTime;
    }
}
