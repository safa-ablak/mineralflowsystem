package be.kdg.prog6.warehousing;

import be.kdg.prog6.warehousing.core.GetWarehouseBalanceUseCaseImpl;
import be.kdg.prog6.warehousing.domain.storage.*;
import be.kdg.prog6.warehousing.port.in.query.GetWarehouseBalanceQuery;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.BalanceSnapshot;
import be.kdg.prog6.warehousing.port.out.BalanceSnapshotPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetWarehouseBalanceUseCaseImplMockingTest {
    private GetWarehouseBalanceUseCaseImpl sut;
    private LoadWarehousePort loadWarehousePort;
    private BalanceSnapshotPort balanceSnapshotPort;

    @BeforeEach
    void setUp() {
        loadWarehousePort = mock(LoadWarehousePort.class);
        balanceSnapshotPort = mock(BalanceSnapshotPort.class);
        sut = new GetWarehouseBalanceUseCaseImpl(loadWarehousePort, balanceSnapshotPort);
    }

    @Test
    void shouldReturnBalanceBasedOnSnapshotPlusActivitiesSinceSnapshot() {
        // Arrange
        final LocalDateTime snapshotTime = TestIds.PERIOD_FROM;
        final LocalDateTime asOf = snapshotTime.plusDays(2).plusSeconds(1);
        final BigDecimal snapshotAmount = BigDecimal.valueOf(500);

        final BalanceSnapshot snapshot = new BalanceSnapshot(
            TestIds.WAREHOUSE_ID_1, snapshotTime, snapshotAmount
        );
        when(balanceSnapshotPort.loadMostRecentSnapshotAsOf(TestIds.WAREHOUSE_ID_1, asOf)).thenReturn(
            Optional.of(snapshot)
        );

        when(loadWarehousePort.loadWarehouseByIdWithActivitiesBetween(TestIds.WAREHOUSE_ID_1, snapshotTime, asOf))
            .thenReturn(Optional.of(createWarehouseWithActivitiesSinceSnapshot(snapshotTime)));

        // Act
        final Balance balance = sut.getBalance(new GetWarehouseBalanceQuery(TestIds.WAREHOUSE_ID_1, asOf));

        // Assert – snapshot (500) + delivery (+100) + shipment (-75) = 525
        assertEquals(asOf, balance.time());
        assertEquals(0, BigDecimal.valueOf(525).compareTo(balance.amount()));
    }

    private static Warehouse createWarehouseWithActivitiesSinceSnapshot(final LocalDateTime snapshotTime) {
        return new Warehouse(
            TestIds.WAREHOUSE_ID_1,
            TestIds.SELLER_ID,
            TestIds.WAREHOUSE_NUMBER_1,
            TestIds.RAW_MATERIAL_1,
            Balance.ORIGIN,
            new StockLedger(
                TestIds.WAREHOUSE_ID_1,
                List.of(
                    new Delivery(TestIds.OLDEST_DELIVERY_ID, snapshotTime.plusDays(1), BigDecimal.valueOf(100))
                ),
                List.of(new Shipment(TestIds.SHIPMENT_ID, snapshotTime.plusDays(2), BigDecimal.valueOf(75))),
                List.of()
            ),
            TestIds.DEFAULT_SITE_LOCATION
        );
    }
}