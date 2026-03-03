package be.kdg.prog6.warehousing.adapter.out.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(catalog = "warehousing", name = "payload_delivery_tickets")
public class PayloadDeliveryTicketJpaEntity {
    @Id
    @Column(name = "ticket_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID ticketId;

    @Column(name = "raw_material", nullable = false)
    private String rawMaterial; // Storing as just a String

    @Column(name = "generation_time", nullable = false)
    private LocalDateTime generationTime;

    @Column(name = "warehouse_number", nullable = false)
    private String warehouseNumber;

    @Column(name = "warehouse_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID warehouseId;

    @Column(name = "dock_number", nullable = false)
    private String dockNumber;

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(final UUID ticketId) {
        this.ticketId = ticketId;
    }

    public String getRawMaterial() {
        return rawMaterial;
    }

    public void setRawMaterial(final String rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public LocalDateTime getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(final LocalDateTime generationTime) {
        this.generationTime = generationTime;
    }

    public String getWarehouseNumber() {
        return warehouseNumber;
    }

    public void setWarehouseNumber(final String warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public String getDockNumber() {
        return dockNumber;
    }

    public void setDockNumber(final String dockNumber) {
        this.dockNumber = dockNumber;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(final UUID warehouseId) {
        this.warehouseId = warehouseId;
    }
}
