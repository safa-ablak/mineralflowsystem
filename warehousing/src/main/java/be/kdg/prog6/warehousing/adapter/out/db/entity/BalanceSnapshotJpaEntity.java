package be.kdg.prog6.warehousing.adapter.out.db.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity representing a historical snapshot of a warehouse's balance.
 * <p>
 * This entity allows querying the balance at specific points in time, which is
 * useful for historical balance calculations and delta-based reporting.
 */
@Entity
@Table(catalog = "warehousing", name = "balance_snapshots")
public class BalanceSnapshotJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Why `Long`?
     * - We use a database-generated sequential `Long` ID because:
     * - It's an internal, non-business identifier (never exposed)
     * - It's faster to index and smaller than a UUID
     * - It avoids UUID fragmentation in index-heavy tables
     * - No need for uniqueness across systems (UUID not necessary)
     */

    @Column(nullable = false, columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID warehouseId;
    /**
     * Business key: which warehouse this snapshot belongs to.
     * - We don't use a foreign key constraint here to keep the table simple and fast for querying.
     */

    @Column(nullable = false)
    private LocalDateTime snapshotTime;
    /**
     * Timestamp of when the balance was recorded.
     * - Indexed (optionally) for fast querying by time
     */

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal balance;
    /**
     * Balance amount in tons at the snapshot time.
     * - Precision: 12 total digits, 4 after the decimal point
     */

    public BalanceSnapshotJpaEntity() {}

    public BalanceSnapshotJpaEntity(final UUID warehouseId, final LocalDateTime snapshotTime, final BigDecimal balance) {
        this.warehouseId = warehouseId;
        this.snapshotTime = snapshotTime;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(final UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public LocalDateTime getSnapshotTime() {
        return snapshotTime;
    }

    public void setSnapshotTime(final LocalDateTime snapshotTime) {
        this.snapshotTime = snapshotTime;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }
}
