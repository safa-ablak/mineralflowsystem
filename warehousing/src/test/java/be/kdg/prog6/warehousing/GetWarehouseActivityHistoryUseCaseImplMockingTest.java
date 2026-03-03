package be.kdg.prog6.warehousing;

import be.kdg.prog6.warehousing.core.GetWarehouseActivityHistoryUseCaseImpl;
import be.kdg.prog6.warehousing.domain.Seller;
import be.kdg.prog6.warehousing.domain.storage.*;
import be.kdg.prog6.warehousing.port.in.query.GetWarehouseActivityHistoryQuery;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetWarehouseActivityHistoryUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.WarehouseActivityHistory;
import be.kdg.prog6.warehousing.port.out.LoadSellerPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetWarehouseActivityHistoryUseCaseImplMockingTest {
    private static final LocalDateTime BASE_TIME =
        LocalDateTime.of(2024, 1, 1, 0, 0);

    private GetWarehouseActivityHistoryUseCase sut;
    private LoadWarehousePort loadWarehousePort;
    private LoadSellerPort loadSellerPort;

    @BeforeEach
    void setUp() {
        loadWarehousePort = mock(LoadWarehousePort.class);
        loadSellerPort = mock(LoadSellerPort.class);
        sut = new GetWarehouseActivityHistoryUseCaseImpl(
            loadWarehousePort,
            loadSellerPort
        );
    }

    private static Warehouse createWarehouseWithAllocations() {
        return new Warehouse(
            TestIds.WAREHOUSE_ID_1,
            TestIds.SELLER_ID,
            TestIds.WAREHOUSE_NUMBER_1,
            TestIds.RAW_MATERIAL_1,
            Balance.ORIGIN,
            new StockLedger(
                TestIds.WAREHOUSE_ID_1,
                List.of(new Delivery(TestIds.OLDEST_DELIVERY_ID, BASE_TIME, BigDecimal.TEN)),
                List.of(new Shipment(TestIds.SHIPMENT_ID, BASE_TIME.plusMinutes(1), BigDecimal.TEN)),
                List.of(new ShipmentAllocation(TestIds.WAREHOUSE_ID_1, TestIds.SHIPMENT_ID, TestIds.OLDEST_DELIVERY_ID, BigDecimal.TEN))
            ),
            TestIds.DEFAULT_SITE_LOCATION
        );
    }

    private static Warehouse createWarehouseWithShuffledActivities() {
        final WarehouseId warehouseId = TestIds.WAREHOUSE_ID_1;
        return new Warehouse(
            warehouseId,
            TestIds.SELLER_ID,
            TestIds.WAREHOUSE_NUMBER_1,
            TestIds.RAW_MATERIAL_1,
            Balance.ORIGIN,
            createStockLedgerWithShuffledActivities(),
            TestIds.DEFAULT_SITE_LOCATION
        );
    }

    private static Seller createSeller() {
        return new Seller(TestIds.SELLER_ID, TestIds.SELLER_NAME, TestIds.SELLER_ADDRESS);
    }

    private static StockLedger createStockLedgerWithShuffledActivities() {
        final List<Delivery> deliveries = List.of(
            deliveryAtMinutes(30),
            deliveryAtMinutes(10),
            deliveryAtMinutes(50),
            deliveryAtMinutes(20),
            deliveryAtMinutes(40)
        );
        final List<Shipment> shipments = List.of(
            shipmentAtMinutes(25),
            shipmentAtMinutes(5),
            shipmentAtMinutes(45),
            shipmentAtMinutes(15),
            shipmentAtMinutes(35)
        );
        return new StockLedger(
            TestIds.WAREHOUSE_ID_1,
            deliveries,
            shipments,
            List.of()
        );
    }

    private static Delivery deliveryAtMinutes(final int minutes) {
        return new Delivery(
            DeliveryId.forWarehouse(TestIds.WAREHOUSE_ID_1),
            BASE_TIME.plusMinutes(minutes),
            BigDecimal.TEN
        );
    }

    private static Shipment shipmentAtMinutes(final int minutes) {
        return new Shipment(
            ShipmentId.forWarehouse(TestIds.WAREHOUSE_ID_1),
            BASE_TIME.plusMinutes(minutes),
            BigDecimal.TEN
        );
    }

    @Test
    void shouldNotIncludeAllocationsInDefaultViewMode() {
        // Arrange
        when(loadWarehousePort.loadWarehouseByIdWithAllActivities(TestIds.WAREHOUSE_ID_1)).thenReturn(
            Optional.of(createWarehouseWithShuffledActivities())
        );
        when(loadSellerPort.loadSellerById(TestIds.SELLER_ID)).thenReturn(
            Optional.of(createSeller())
        );

        // Act
        final GetWarehouseActivityHistoryQuery query = new GetWarehouseActivityHistoryQuery(
            TestIds.WAREHOUSE_ID_1
        );
        final WarehouseActivityHistory history = sut.getWarehouseActivityHistory(
            query
        );

        // Assert
        assertTrue(history.allocations().isEmpty());
    }

    @Test
    void shouldIncludeAllocationsWhenRequested() {
        // Arrange
        when(loadWarehousePort.loadWarehouseByIdWithAllActivitiesAndAllocations(TestIds.WAREHOUSE_ID_1)).thenReturn(
            Optional.of(createWarehouseWithAllocations())
        );
        when(loadSellerPort.loadSellerById(TestIds.SELLER_ID)).thenReturn(
            Optional.of(createSeller())
        );

        // Act
        final GetWarehouseActivityHistoryQuery query = new GetWarehouseActivityHistoryQuery(
            TestIds.WAREHOUSE_ID_1,
            GetWarehouseActivityHistoryQuery.ViewMode.WITH_ALLOCATIONS
        );
        final WarehouseActivityHistory history = sut.getWarehouseActivityHistory(
            query
        );

        // Assert
        assertEquals(
            List.of(new ShipmentAllocation(TestIds.WAREHOUSE_ID_1, TestIds.SHIPMENT_ID, TestIds.OLDEST_DELIVERY_ID, BigDecimal.TEN)),
            history.allocations()
        );
    }

    @Test
    void shouldReturnNewestActivitiesFirst() {
        // Arrange
        when(loadWarehousePort.loadWarehouseByIdWithAllActivities(TestIds.WAREHOUSE_ID_1)).thenReturn(Optional.of(
            createWarehouseWithShuffledActivities()
        ));
        when(loadSellerPort.loadSellerById(TestIds.SELLER_ID)).thenReturn(Optional.of(
            createSeller()
        ));
        final GetWarehouseActivityHistoryQuery query = new GetWarehouseActivityHistoryQuery(
            TestIds.WAREHOUSE_ID_1
        );

        // Act
        final WarehouseActivityHistory warehouseActivityHistory = sut.getWarehouseActivityHistory(query);

        // Assert
        final List<Delivery> deliveries = warehouseActivityHistory.deliveries();
        assertEquals(5, deliveries.size());
        for (int i = 0; i < deliveries.size() - 1; i++) {
            final LocalDateTime current = deliveries.get(i).time();
            final LocalDateTime next = deliveries.get(i + 1).time();
            assertTrue(
                current.isAfter(next),
                "Deliveries are not ordered strictly from newest to oldest"
            );
        }
        final List<Shipment> shipments = warehouseActivityHistory.shipments();
        assertEquals(5, shipments.size());
        for (int i = 0; i < shipments.size() - 1; i++) {
            final LocalDateTime current = shipments.get(i).time();
            final LocalDateTime next = shipments.get(i + 1).time();
            assertTrue(
                current.isAfter(next),
                "Shipments are not ordered strictly from newest to oldest"
            );
        }
    }
}
