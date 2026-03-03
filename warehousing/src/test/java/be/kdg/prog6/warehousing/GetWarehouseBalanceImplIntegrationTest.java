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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetWarehouseBalanceImplIntegrationTest extends AbstractDatabaseTest {
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
    }

    // Timeline: snapshot (day 0), delivery +100 (day 1), shipment -75 (day 2), delivery +50 (day 3)
    // asOf = day 2.5: the day 3 delivery should NOT be included
    @Test
    void shouldExcludeActivitiesAfterAsOf() {
        // Arrange
        final LocalDateTime snapshotTime = TestIds.PERIOD_FROM;
        final LocalDateTime asOf = snapshotTime.plusDays(2).plusHours(12);
        final UUID warehouseId = TestIds.WAREHOUSE_ID_1.id();

        balanceSnapshotJpaRepository.save(
            new BalanceSnapshotJpaEntity(warehouseId, snapshotTime, BigDecimal.valueOf(500))
        );
        warehouseDeliveryJpaRepository.save(createDelivery(TestIds.OLDEST_DELIVERY_ID.id(), snapshotTime.plusDays(1), BigDecimal.valueOf(100)));
        warehouseShipmentJpaRepository.save(createShipment(TestIds.SHIPMENT_ID.id(), snapshotTime.plusDays(2), BigDecimal.valueOf(75)));
        warehouseDeliveryJpaRepository.save(createDelivery(TestIds.MIDDLE_DELIVERY_ID.id(), snapshotTime.plusDays(3), BigDecimal.valueOf(50)));

        // Act
        final Balance balance = getWarehouseBalanceUseCase.getBalance(
            new GetWarehouseBalanceQuery(TestIds.WAREHOUSE_ID_1, asOf)
        );

        // Assert: snapshot (500) + delivery (+100) + shipment (-75) = 525, day 3 delivery excluded
        assertEquals(0, BigDecimal.valueOf(525).compareTo(balance.amount()));
    }

    // Two snapshots: day 0 (500) and day 4 (800), asOf = day 2.5
    // Should pick the day 0 snapshot, not the day 4 one
    @Test
    void shouldPickMostRecentSnapshotBeforeAsOf() {
        // Arrange
        final LocalDateTime baseTime = TestIds.PERIOD_FROM;
        final LocalDateTime asOf = baseTime.plusDays(2).plusHours(12);
        final UUID warehouseId = TestIds.WAREHOUSE_ID_1.id();

        balanceSnapshotJpaRepository.save(new BalanceSnapshotJpaEntity(warehouseId, baseTime, BigDecimal.valueOf(500)));
        balanceSnapshotJpaRepository.save(new BalanceSnapshotJpaEntity(warehouseId, baseTime.plusDays(4), BigDecimal.valueOf(800)));

        warehouseDeliveryJpaRepository.save(createDelivery(TestIds.OLDEST_DELIVERY_ID.id(), baseTime.plusDays(1), BigDecimal.valueOf(100)));

        // Act
        final Balance balance = getWarehouseBalanceUseCase.getBalance(
            new GetWarehouseBalanceQuery(TestIds.WAREHOUSE_ID_1, asOf)
        );

        // Assert: day 0 snapshot (500) + delivery (+100) = 600, not 800-based
        assertEquals(0, BigDecimal.valueOf(600).compareTo(balance.amount()));
    }

    private static WarehouseJpaEntity createEmptyWarehouse() {
        final WarehouseJpaEntity warehouse = new WarehouseJpaEntity();
        warehouse.setWarehouseId(TestIds.WAREHOUSE_ID_1.id());
        warehouse.setSellerId(TestIds.SELLER_ID.id());
        warehouse.setWarehouseNumber(TestIds.WAREHOUSE_NUMBER_1.value());
        warehouse.setRawMaterial(RawMaterial.GYPSUM);
        warehouse.setBalance(BigDecimal.ZERO);
        warehouse.setPercentageFilled(BigDecimal.ZERO);
        warehouse.setBalanceUpdatedAt(TestIds.PERIOD_FROM);
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

    private static WarehouseShipmentJpaEntity createShipment(final UUID shipmentId, final LocalDateTime time, final BigDecimal amount) {
        final WarehouseShipmentJpaEntity shipment = new WarehouseShipmentJpaEntity();
        shipment.setId(new WarehouseShipmentJpaId(TestIds.WAREHOUSE_ID_1.id(), shipmentId));
        shipment.setTime(time);
        shipment.setAmount(amount);
        return shipment;
    }
}
