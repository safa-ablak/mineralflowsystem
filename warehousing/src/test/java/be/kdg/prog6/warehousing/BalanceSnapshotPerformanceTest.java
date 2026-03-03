package be.kdg.prog6.warehousing;

import be.kdg.prog6.warehousing.adapter.out.db.entity.*;
import be.kdg.prog6.warehousing.adapter.out.db.repository.BalanceSnapshotJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.repository.WarehouseDeliveryJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.repository.WarehouseJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.repository.WarehouseShipmentJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.value.SiteLocationEmbeddable;
import be.kdg.prog6.warehousing.domain.storage.Balance;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.port.in.query.GetWarehouseBalanceQuery;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetWarehouseBalanceUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
* Performance test: demonstrates that balance snapshots reduce the number of
* activities the query needs to replay, significantly speeding up balance calculation.
*/
public class BalanceSnapshotPerformanceTest extends AbstractDatabaseTest {
    private static final int TOTAL_DELIVERIES = 50_000;
    private static final int ACTIVITIES_AFTER_SNAPSHOT = 100;
    private static final LocalDateTime START_TIME = LocalDateTime.of(
        2025, 1, 1, 0, 0
    );
    private static final LocalDateTime AS_OF = START_TIME.plusMinutes(TOTAL_DELIVERIES + 1);

    @Autowired
    private GetWarehouseBalanceUseCase getWarehouseBalanceUseCase;
    @Autowired
    private WarehouseJpaRepository warehouseJpaRepository;
    @Autowired
    private WarehouseDeliveryJpaRepository warehouseDeliveryJpaRepository;
    @Autowired
    private WarehouseShipmentJpaRepository warehouseShipmentJpaRepository;
    @Autowired
    private BalanceSnapshotJpaRepository balanceSnapshotJpaRepository;

    @BeforeEach
    void setUp() {
        warehouseDeliveryJpaRepository.deleteAll();
        warehouseShipmentJpaRepository.deleteAll();
        balanceSnapshotJpaRepository.deleteAll();
        warehouseJpaRepository.deleteAll();
        warehouseJpaRepository.save(createEmptyWarehouse());
        seedDeliveries();
    }

    // 50,000 deliveries of 1 ton each, one per minute
    // WITHOUT snapshot: replays all 50,000 activities from ORIGIN
    // WITH snapshot at minute 49,900 (balance = 49,900): replays only ~100 activities
    // Both must return balance = 50,000
    @Test
    void snapshotShouldSignificantlyReduceBalanceCalculationTime() {
        // Arrange
        final UUID warehouseId = TestIds.WAREHOUSE_ID_1.id();

        // Act 1: balance WITHOUT snapshot – replays all 50,000 activities
        final long withoutSnapshotStart = System.nanoTime();
        final Balance balanceWithoutSnapshot = getWarehouseBalanceUseCase.getBalance(
            new GetWarehouseBalanceQuery(TestIds.WAREHOUSE_ID_1, AS_OF)
        );
        final long withoutSnapshotMs = (System.nanoTime() - withoutSnapshotStart) / 1_000_000;

        // Insert a snapshot near the end, leaving only ~100 activities to replay.
        // +30s avoids inclusive BETWEEN overlap with the delivery at exactly that minute.
        final int snapshotMinute = TOTAL_DELIVERIES - ACTIVITIES_AFTER_SNAPSHOT;
        balanceSnapshotJpaRepository.save(new BalanceSnapshotJpaEntity(
            warehouseId,
            START_TIME.plusMinutes(snapshotMinute).plusSeconds(30),
            BigDecimal.valueOf(snapshotMinute)
        ));

        // Act 2: balance WITH snapshot – replays only ~100 activities
        final long withSnapshotStart = System.nanoTime();
        final Balance balanceWithSnapshot = getWarehouseBalanceUseCase.getBalance(
            new GetWarehouseBalanceQuery(TestIds.WAREHOUSE_ID_1, AS_OF)
        );
        final long withSnapshotMs = (System.nanoTime() - withSnapshotStart) / 1_000_000;

        // Assert correctness: both paths must yield the same result
        final BigDecimal expectedBalance = BigDecimal.valueOf(TOTAL_DELIVERIES);
        assertEquals(0, expectedBalance.compareTo(balanceWithoutSnapshot.amount()),
            "Balance without snapshot should equal total deliveries");
        assertEquals(0, expectedBalance.compareTo(balanceWithSnapshot.amount()),
            "Balance with snapshot should equal total deliveries");

        // Assert performance: snapshot path should be faster
        System.out.printf("Without snapshot: %d ms (%d activities replayed)%n", withoutSnapshotMs, TOTAL_DELIVERIES);
        System.out.printf("With snapshot:    %d ms (~%d activities replayed)%n", withSnapshotMs, ACTIVITIES_AFTER_SNAPSHOT);
        System.out.printf("Speedup factor:   %dx%n", withoutSnapshotMs / Math.max(withSnapshotMs, 1));

        assertTrue(
            withSnapshotMs < withoutSnapshotMs,
            "Balance calculation with snapshot should be faster than without. Without: %d ms, With: %d ms"
                .formatted(withoutSnapshotMs, withSnapshotMs)
        );
    }

    private void seedDeliveries() {
        final List<WarehouseDeliveryJpaEntity> deliveries = new ArrayList<>(TOTAL_DELIVERIES);
        for (int i = 0; i < TOTAL_DELIVERIES; i++) {
            deliveries.add(createDelivery(UUID.randomUUID(), START_TIME.plusMinutes(i + 1), BigDecimal.ONE));
        }
        warehouseDeliveryJpaRepository.saveAll(deliveries);
    }

    private static WarehouseJpaEntity createEmptyWarehouse() {
        final WarehouseJpaEntity warehouse = new WarehouseJpaEntity();
        warehouse.setWarehouseId(TestIds.WAREHOUSE_ID_1.id());
        warehouse.setSellerId(TestIds.SELLER_ID.id());
        warehouse.setWarehouseNumber(TestIds.WAREHOUSE_NUMBER_1.value());
        warehouse.setRawMaterial(RawMaterial.GYPSUM);
        warehouse.setBalance(BigDecimal.ZERO);
        warehouse.setPercentageFilled(BigDecimal.ZERO);
        warehouse.setBalanceUpdatedAt(START_TIME);
        warehouse.setSiteLocation(new SiteLocationEmbeddable(0.0, 0.0));
        return warehouse;
    }

    private static WarehouseDeliveryJpaEntity createDelivery(final UUID deliveryId, final LocalDateTime time, final BigDecimal amount) {
        final WarehouseDeliveryJpaEntity delivery = new WarehouseDeliveryJpaEntity();
        delivery.setId(new WarehouseDeliveryJpaId(TestIds.WAREHOUSE_ID_1.id(), deliveryId));
        delivery.setTime(time);
        delivery.setAmount(amount);
        return delivery;
    }
}