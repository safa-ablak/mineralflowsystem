package be.kdg.prog6.warehousing;

import be.kdg.prog6.warehousing.core.ReportStorageForWarehousesTaskImpl;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.storage.*;
import be.kdg.prog6.warehousing.port.out.LoadSellerPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import be.kdg.prog6.warehousing.port.out.SellerWarehousesStorageReportedPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReportStorageForWarehousesTaskImplMockingTest {
    private ReportStorageForWarehousesTaskImpl sut;
    private LoadSellerPort loadSellerPort;
    private LoadWarehousePort loadWarehousePort;
    private SellerWarehousesStorageReportedPort sellerWarehousesStorageReportedPort;

    private static final Clock FIXED_CLOCK = Clock.fixed(
        TestIds.REPORTING_DATE_TIME.toInstant(ZoneOffset.UTC),
        ZoneOffset.UTC
    );

    @BeforeEach
    void setUp() {
        loadSellerPort = mock(LoadSellerPort.class);
        loadWarehousePort = mock(LoadWarehousePort.class);
        sellerWarehousesStorageReportedPort = mock(SellerWarehousesStorageReportedPort.class);
        sut = new ReportStorageForWarehousesTaskImpl(
            loadSellerPort,
            loadWarehousePort,
            sellerWarehousesStorageReportedPort,
            FIXED_CLOCK
        );
    }

    @Test
    void shouldReportStoragePerSellerAndIncludeCorrectStoredDeliveryRemainders() {
        // Arrange
        when(loadSellerPort.loadAllSellerIds()).thenReturn(
            List.of(TestIds.SELLER_ID)
        );
        final Warehouse warehouse = createWarehouseWithAgedDeliveries();
        when(loadWarehousePort.loadWarehousesBySellerIdForReporting(TestIds.SELLER_ID)).thenReturn(
            List.of(warehouse)
        );

        // Act
        sut.reportStorageForWarehouses();

        // Assert
        final ArgumentCaptor<SellerId> sellerIdCaptor = ArgumentCaptor.forClass(SellerId.class);
        final ArgumentCaptor<List<Warehouse>> reportedWarehousesCaptor = ArgumentCaptor.forClass(List.class);
        final ArgumentCaptor<LocalDateTime> reportingDateTimeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        verify(sellerWarehousesStorageReportedPort, times(1)).storageReported(
            sellerIdCaptor.capture(),
            reportedWarehousesCaptor.capture(),
            reportingDateTimeCaptor.capture()
        );
        final SellerId capturedSellerId = sellerIdCaptor.getValue();
        final List<Warehouse> warehousesThatWerePassedToTheStorageReportedMethod = reportedWarehousesCaptor.getValue();
        final LocalDateTime capturedReportingDateTime = reportingDateTimeCaptor.getValue();

        assertEquals(TestIds.SELLER_ID, capturedSellerId);
        assertEquals(1, warehousesThatWerePassedToTheStorageReportedMethod.size());
        assertEquals(warehouse, warehousesThatWerePassedToTheStorageReportedMethod.getFirst());
        assertEquals(TestIds.REPORTING_DATE_TIME, capturedReportingDateTime);
        // Stored delivery remainders assertions
        final Warehouse reportedWarehouse = warehousesThatWerePassedToTheStorageReportedMethod.getFirst();
        final List<StoredDeliveryRemainder> remainders = reportedWarehouse.getStoredDeliveryRemainders(
            capturedReportingDateTime
        );
        assertThat(remainders).containsExactlyInAnyOrder(
            new StoredDeliveryRemainder(TestIds.OLDEST_DELIVERY_ID, BigDecimal.valueOf(5), 3),
            new StoredDeliveryRemainder(TestIds.MIDDLE_DELIVERY_ID, BigDecimal.valueOf(10), 2),
            new StoredDeliveryRemainder(TestIds.UNAGED_DELIVERY_ID, BigDecimal.valueOf(20), 0)
        );
    }

    private static Warehouse createWarehouseWithAgedDeliveries() {
        return new Warehouse(
            TestIds.WAREHOUSE_ID_1,
            TestIds.SELLER_ID,
            TestIds.WAREHOUSE_NUMBER_1,
            TestIds.RAW_MATERIAL_1,
            Balance.ORIGIN,
            createStockLedgerWithAgedDeliveries(),
            TestIds.DEFAULT_SITE_LOCATION
        );
    }

    private static StockLedger createStockLedgerWithAgedDeliveries() {
        // <5, 3> (10 total, 5 allocated -> 5 remaining)
        final Delivery oldAgedDelivery = new Delivery(
            TestIds.OLDEST_DELIVERY_ID,
            TestIds.REPORTING_DATE_TIME.minusDays(3),
            BigDecimal.valueOf(10)
        );
        // <10, 2> (untouched)
        final Delivery middleAgedDelivery = new Delivery(
            TestIds.MIDDLE_DELIVERY_ID,
            TestIds.REPORTING_DATE_TIME.minusDays(2),
            BigDecimal.valueOf(10)
        );
        // <20, 0> (unaged, will still be reported)
        final Delivery unagedDelivery = new Delivery(
            TestIds.UNAGED_DELIVERY_ID,
            TestIds.REPORTING_DATE_TIME.minusDays(1).plusSeconds(1),
            BigDecimal.valueOf(20)
        );
        // This delivery will not be reported because its time is after the reporting date.
        // This behavior is enforced in the domain layer.
        final Delivery deliveryAfterReportingDateTime = new Delivery(
            TestIds.AFTER_REPORTING_DATE_TIME_DELIVERY_ID,
            TestIds.REPORTING_DATE_TIME.plusDays(1),
            BigDecimal.valueOf(999)
        );
        final ShipmentAllocation shipmentAllocation = new ShipmentAllocation(
            TestIds.WAREHOUSE_ID_1,
            TestIds.SHIPMENT_ID,
            TestIds.OLDEST_DELIVERY_ID,
            BigDecimal.valueOf(5) // 5 allocated from the old aged delivery
        );
        return new StockLedger(
            TestIds.WAREHOUSE_ID_1,
            List.of(
                oldAgedDelivery,
                middleAgedDelivery,
                unagedDelivery,
                deliveryAfterReportingDateTime
            ),
            List.of(), // Shipments are irrelevant for delivery age reporting
            List.of(shipmentAllocation)
        );
    }
}
