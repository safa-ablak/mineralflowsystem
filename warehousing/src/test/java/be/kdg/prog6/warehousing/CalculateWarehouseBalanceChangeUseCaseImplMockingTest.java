package be.kdg.prog6.warehousing;

import be.kdg.prog6.warehousing.core.CalculateWarehouseBalanceChangeUseCaseImpl;
import be.kdg.prog6.warehousing.domain.storage.*;
import be.kdg.prog6.warehousing.port.in.query.CalculateWarehouseBalanceChangeQuery;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.NetBalanceChange;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CalculateWarehouseBalanceChangeUseCaseImplMockingTest {
    private CalculateWarehouseBalanceChangeUseCaseImpl sut;
    private LoadWarehousePort loadWarehousePort;

    @BeforeEach
    void setUp() {
        loadWarehousePort = mock(LoadWarehousePort.class);
        sut = new CalculateWarehouseBalanceChangeUseCaseImpl(loadWarehousePort);
    }

    @Test
    void shouldReturnNegativeNetBalanceChangeWhenShipmentsExceedDeliveriesWithinPeriod() {
        // Arrange
        when(loadWarehousePort.loadWarehouseByIdWithActivitiesBetween(
            TestIds.WAREHOUSE_ID_1,
            TestIds.PERIOD_FROM,
            TestIds.PERIOD_TO
        )).thenReturn(Optional.of(
            createWarehouseWithDeliveriesAndShipmentsWithinPeriod()
        ));
        final CalculateWarehouseBalanceChangeQuery query = new CalculateWarehouseBalanceChangeQuery(
                TestIds.WAREHOUSE_ID_1,
                TestIds.PERIOD_FROM,
                TestIds.PERIOD_TO
        );

        // Act
        final NetBalanceChange result = sut.calculateNetBalanceChange(query);

        // Assert
        // 10 (deliveries) - 15 (shipments) = -5 net change
        assertEquals(TestIds.WAREHOUSE_ID_1, result.warehouseId());
        assertEquals(TestIds.PERIOD_FROM, result.from());
        assertEquals(TestIds.PERIOD_TO, result.to());
        assertEquals(BigDecimal.valueOf(-5), result.netBalanceChange());
    }

    private static Warehouse createWarehouseWithDeliveriesAndShipmentsWithinPeriod() {
        return new Warehouse(
            TestIds.WAREHOUSE_ID_1,
            TestIds.SELLER_ID,
            TestIds.WAREHOUSE_NUMBER_1,
            TestIds.RAW_MATERIAL_1,
            Balance.ORIGIN,
            createStockLedgerWithTwoDeliveriesAndOneShipment(),
            TestIds.DEFAULT_SITE_LOCATION
        );
    }

    private static StockLedger createStockLedgerWithTwoDeliveriesAndOneShipment() {
        final Delivery firstDeliveryWithinPeriod = new Delivery(
            TestIds.MIDDLE_DELIVERY_ID,
            TestIds.PERIOD_FROM.plusDays(1),
            BigDecimal.valueOf(5)
        );
        final Delivery secondDeliveryWithinPeriod = new Delivery(
            TestIds.NEWEST_DELIVERY_ID,
            TestIds.PERIOD_FROM.plusDays(2),
            BigDecimal.valueOf(5)
        );
        final Shipment shipmentWithinPeriod = new Shipment(
            TestIds.SHIPMENT_ID,
            TestIds.PERIOD_FROM.plusDays(3),
            BigDecimal.valueOf(15)
        );
        return new StockLedger(
            TestIds.WAREHOUSE_ID_1,
            List.of(firstDeliveryWithinPeriod, secondDeliveryWithinPeriod),
            List.of(shipmentWithinPeriod),
            // Allocations are irrelevant here since balance change is based on delta of deliveries and shipments
            List.of()
        );
    }
}
