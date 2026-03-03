package be.kdg.prog6.warehousing;

import be.kdg.prog6.common.event.warehousing.PurchaseOrderShippedEvent;
import be.kdg.prog6.warehousing.adapter.out.publisher.PurchaseOrderShippedPublisher;
import be.kdg.prog6.warehousing.core.ShipPurchaseOrderUseCaseImpl;
import be.kdg.prog6.warehousing.domain.purchaseorder.OrderLine;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.domain.storage.*;
import be.kdg.prog6.warehousing.port.in.command.ShipPurchaseOrderCommand;
import be.kdg.prog6.warehousing.port.out.LoadPurchaseOrderPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import be.kdg.prog6.warehousing.port.out.ShipmentRecordedPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ShipPurchaseOrderUseCaseImplMockingTest {
    private ShipPurchaseOrderUseCaseImpl sut;
    private LoadPurchaseOrderPort loadPurchaseOrderPort;
    private LoadWarehousePort loadWarehousePort;
    private ShipmentRecordedPort shipmentRecordedPort;
    private PurchaseOrderShippedPublisher publisher;

    @BeforeEach
    void setUp() {
        shipmentRecordedPort = mock(ShipmentRecordedPort.class);
        loadPurchaseOrderPort = mock(LoadPurchaseOrderPort.class);
        loadWarehousePort = mock(LoadWarehousePort.class);
        publisher = mock(PurchaseOrderShippedPublisher.class);
        sut = new ShipPurchaseOrderUseCaseImpl(
            loadPurchaseOrderPort,
            loadWarehousePort,
            List.of(shipmentRecordedPort),
            publisher
        );
    }

    // Basic scenario: Testing Delivery allocation order for a single line Purchase Order
    @Test
    void shouldShipFromOldestDeliveriesFirst() {
        // Arrange
        when(loadPurchaseOrderPort.loadPurchaseOrderById(TestIds.PURCHASE_ORDER_ID_SINGLE_LINE)).thenReturn(Optional.of(
            createPurchaseOrderWithASingleLine()
        ));
        when(loadWarehousePort.loadWarehousesBySellerIdForShipment(TestIds.SELLER_ID)).thenReturn(
            List.of(createWarehouseWithDeliveries())
        );

        // Act
        sut.shipPurchaseOrder(new ShipPurchaseOrderCommand(TestIds.PURCHASE_ORDER_ID_SINGLE_LINE));

        // Assert
        final ArgumentCaptor<Warehouse> warehouseCaptor = ArgumentCaptor.forClass(Warehouse.class);
        final ArgumentCaptor<ShipmentRecord> recordCaptor = ArgumentCaptor.forClass(ShipmentRecord.class);
        // Verify that the shipment was recorded
        verify(shipmentRecordedPort).shipmentRecorded(warehouseCaptor.capture(), recordCaptor.capture());

        final ShipmentRecord record = recordCaptor.getValue();
        // No need to sort: allocations are already in FIFO order
        final List<UUID> actualOrder = getActualDeliveryAllocationOrder(record);
        final List<UUID> expectedOrder = getExpectedDeliveryAllocationOrder();

        // Assert warehouse balance (200 - 180 = 20)
        assertEquals(warehouseCaptor.getValue().balance(), BigDecimal.valueOf(20));
        // Assert correct order for deliveries used
        assertEquals(expectedOrder, actualOrder);
        // Assert amounts allocated from deliveries [100, 50, 30]
        assertEquals(BigDecimal.valueOf(100), record.allocations().get(0).amountAllocated());
        assertEquals(BigDecimal.valueOf(50), record.allocations().get(1).amountAllocated());
        assertEquals(BigDecimal.valueOf(30), record.allocations().get(2).amountAllocated());
        // Assert correct shipment amount 180
        assertEquals(BigDecimal.valueOf(180), record.shipment().amount());
    }

    private static List<UUID> getActualDeliveryAllocationOrder(final ShipmentRecord record) {
        return record.allocations().stream()
            .map(a -> a.deliveryId().id())
            .toList();
    }

    // Complex scenario: Testing shipment of a 5-line Purchase Order involving 5 warehouses
    @Test
    void shouldShipFromFiveWarehousesEmptyAllAndPublish() {
        // Arrange
        final int warehouseCount = 5;
        when(loadPurchaseOrderPort.loadPurchaseOrderById(TestIds.PURCHASE_ORDER_ID_FIVE_LINES)).thenReturn(Optional.of(
            createPurchaseOrderWithFiveLines()
        ));
        when(loadWarehousePort.loadWarehousesBySellerIdForShipment(TestIds.SELLER_ID)).thenReturn(
            createFiveWarehousesWithDeliveries()
        );

        // Act
        sut.shipPurchaseOrder(new ShipPurchaseOrderCommand(TestIds.PURCHASE_ORDER_ID_FIVE_LINES));

        // Assert
        final ArgumentCaptor<Warehouse> warehouseCaptor = ArgumentCaptor.forClass(Warehouse.class);
        final ArgumentCaptor<ShipmentRecord> recordCaptor = ArgumentCaptor.forClass(ShipmentRecord.class);
        verify(shipmentRecordedPort, times(warehouseCount)).shipmentRecorded(
            warehouseCaptor.capture(), recordCaptor.capture()
        );
        final List<Warehouse> capturedWarehouses = warehouseCaptor.getAllValues();
        final List<ShipmentRecord> capturedRecords = recordCaptor.getAllValues();

        for (int i = 0; i < warehouseCount; i++) {
            final Warehouse warehouse = capturedWarehouses.get(i);
            final ShipmentRecord record = capturedRecords.get(i);

            assertEquals(0, BigDecimal.valueOf(30_000).compareTo(record.shipment().amount()));
            assertEquals(1200, record.allocations().size());

            final BigDecimal totalAllocated = getTotalAllocated(record);

            assertEquals(0, BigDecimal.valueOf(30_000).compareTo(totalAllocated));
            assertTrue(warehouse.isEmpty());
        }
        // Verify that the PurchaseOrderShippedEvent is published with the correct ID
        final ArgumentCaptor<PurchaseOrderShippedEvent> eventCaptor = ArgumentCaptor.forClass(PurchaseOrderShippedEvent.class);
        verify(publisher).purchaseOrderShipped(eventCaptor.capture());
        assertEquals(TestIds.PURCHASE_ORDER_ID_FIVE_LINES.id(), eventCaptor.getValue().purchaseOrderId());
    }

    private static BigDecimal getTotalAllocated(final ShipmentRecord record) {
        return record.allocations().stream()
            .map(ShipmentAllocation::amountAllocated)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static List<UUID> getExpectedDeliveryAllocationOrder() {
        return List.of(
            TestIds.OLDEST_DELIVERY_ID.id(),
            TestIds.MIDDLE_DELIVERY_ID.id(),
            TestIds.NEWEST_DELIVERY_ID.id()
        );
    }

    private static PurchaseOrder createPurchaseOrderWithASingleLine() {
        final OrderLine orderLine = new OrderLine(
            TestIds.RAW_MATERIAL_1,
            BigDecimal.valueOf(180)
        );
        return new PurchaseOrder(
            TestIds.PURCHASE_ORDER_NUMBER_SINGLE_LINE,
            TestIds.BUYER_ID,
            TestIds.BUYER_NAME,
            TestIds.SELLER_ID,
            TestIds.SELLER_NAME,
            List.of(orderLine)
        );
    }

    private static PurchaseOrder createPurchaseOrderWithFiveLines() {
        final List<OrderLine> orderLines = List.of(
            new OrderLine(TestIds.RAW_MATERIAL_1, BigDecimal.valueOf(30_000)),
            new OrderLine(TestIds.RAW_MATERIAL_2, BigDecimal.valueOf(30_000)),
            new OrderLine(TestIds.RAW_MATERIAL_3, BigDecimal.valueOf(30_000)),
            new OrderLine(TestIds.RAW_MATERIAL_4, BigDecimal.valueOf(30_000)),
            new OrderLine(TestIds.RAW_MATERIAL_5, BigDecimal.valueOf(30_000))
        );
        return new PurchaseOrder(
            TestIds.PURCHASE_ORDER_NUMBER_FIVE_LINES,
            TestIds.BUYER_ID,
            TestIds.BUYER_NAME,
            TestIds.SELLER_ID,
            TestIds.SELLER_NAME,
            orderLines
        );
    }

    private static Warehouse createWarehouseWithDeliveries() {
        final Balance baseBalance = Balance.ORIGIN;
        return new Warehouse(
            TestIds.WAREHOUSE_ID_1,
            TestIds.SELLER_ID,
            TestIds.WAREHOUSE_NUMBER_1,
            TestIds.RAW_MATERIAL_1,
            baseBalance,
            new StockLedger(
                TestIds.WAREHOUSE_ID_1,
                createDeliveries(baseBalance.time()),
                new ArrayList<>(),
                new ArrayList<>()
            ),
            TestIds.DEFAULT_SITE_LOCATION
        );
    }

    private static List<Delivery> createDeliveries(final LocalDateTime baseTime) {
        // Note: Creating them in reverse order on purpose here to be able to verify
        // that FIFO allocation logic (deliveries being sorted by oldest first) in the domain works properly regardless
        // of order.
        return List.of(
            new Delivery(
                TestIds.NEWEST_DELIVERY_ID,
                baseTime.plusDays(3),
                BigDecimal.valueOf(50)
            ),
            new Delivery(
                TestIds.MIDDLE_DELIVERY_ID,
                baseTime.plusDays(2),
                BigDecimal.valueOf(50)
            ),
            new Delivery(
                TestIds.OLDEST_DELIVERY_ID,
                baseTime.plusDays(1),
                BigDecimal.valueOf(100)
            )
        );
    }

    private static List<Warehouse> createFiveWarehousesWithDeliveries() {
        return List.of(
            createWarehouseWithDeliveries(TestIds.WAREHOUSE_ID_1, TestIds.WAREHOUSE_NUMBER_1, TestIds.RAW_MATERIAL_1),
            createWarehouseWithDeliveries(TestIds.WAREHOUSE_ID_2, TestIds.WAREHOUSE_NUMBER_2, TestIds.RAW_MATERIAL_2),
            createWarehouseWithDeliveries(TestIds.WAREHOUSE_ID_3, TestIds.WAREHOUSE_NUMBER_3, TestIds.RAW_MATERIAL_3),
            createWarehouseWithDeliveries(TestIds.WAREHOUSE_ID_4, TestIds.WAREHOUSE_NUMBER_4, TestIds.RAW_MATERIAL_4),
            createWarehouseWithDeliveries(TestIds.WAREHOUSE_ID_5, TestIds.WAREHOUSE_NUMBER_5, TestIds.RAW_MATERIAL_5)
        );
    }

    private static Warehouse createWarehouseWithDeliveries(final WarehouseId warehouseId,
                                                           final WarehouseNumber warehouseNumber,
                                                           final RawMaterial rawMaterial) {
        final Balance baseBalance = Balance.ORIGIN;
        return new Warehouse(
            warehouseId,
            TestIds.SELLER_ID,
            warehouseNumber,
            rawMaterial,
            baseBalance,
            new StockLedger(
                warehouseId,
                // Most popular truck is 25t so for 30_000t we need 1200 deliveries
                createDeliveries(warehouseId, baseBalance.time(), BigDecimal.valueOf(25), 1200),
                new ArrayList<>(),
                new ArrayList<>()
            ),
            TestIds.DEFAULT_SITE_LOCATION
        );
    }

    private static List<Delivery> createDeliveries(final WarehouseId warehouseId,
                                                   final LocalDateTime baseTime,
                                                   final BigDecimal amount,
                                                   final int times) {
        final List<Delivery> deliveries = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            deliveries.add(
                new Delivery(
                    DeliveryId.forWarehouse(warehouseId),
                    baseTime.plusSeconds(i + 1),
                    amount
                )
            );
        }
        return deliveries;
    }
}