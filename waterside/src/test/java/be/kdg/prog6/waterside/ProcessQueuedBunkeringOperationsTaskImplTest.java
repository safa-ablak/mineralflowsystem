package be.kdg.prog6.waterside;

import be.kdg.prog6.waterside.core.ProcessQueuedBunkeringOperationsTaskImpl;
import be.kdg.prog6.waterside.domain.*;
import be.kdg.prog6.waterside.port.out.BunkeringOperationQueryPort;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import be.kdg.prog6.waterside.port.out.UpdateShippingOrderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static be.kdg.prog6.waterside.TestIds.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ProcessQueuedBunkeringOperationsTaskImplTest {
    private ProcessQueuedBunkeringOperationsTaskImpl sut;
    private LoadShippingOrderPort loadShippingOrderPort;
    private BunkeringOperationQueryPort bunkeringOperationQueryPort;
    private UpdateShippingOrderPort updateShippingOrderPort;

    private static final Clock FIXED_CLOCK = Clock.fixed(
        TestIds.PROCESSING_DATE.atStartOfDay().toInstant(ZoneOffset.UTC),
        ZoneOffset.UTC
    );

    @BeforeEach
    void setUp() {
        loadShippingOrderPort = mock(LoadShippingOrderPort.class);
        bunkeringOperationQueryPort = mock(BunkeringOperationQueryPort.class);
        updateShippingOrderPort = mock(UpdateShippingOrderPort.class);
        sut = new ProcessQueuedBunkeringOperationsTaskImpl(
            loadShippingOrderPort,
            bunkeringOperationQueryPort,
            updateShippingOrderPort,
            FIXED_CLOCK
        );
    }

    // Current limit is 6 per day
    @Test
    void shouldProcessQueuedBunkeringOperationsInFIFOOrderAndRespectDailyLimit() {
        // Arrange
        final int performedToday = 3;

        // Simulate 3 bunkering operations already performed today
        when(bunkeringOperationQueryPort.countPerformedBunkeringOperationsByDate(TestIds.PROCESSING_DATE)).thenReturn(
            performedToday
        );
        // Simulated DB result: Normally the repository returns ShippingOrders in FIFO order (oldest queued first).
        // In this test, we deliberately shuffle the list to demonstrate that the scheduled task
        // relies on the repository to provide the correct order, rather than sorting internally.
        when(loadShippingOrderPort.loadShippingOrdersWithQueuedBunkeringByOldestFirst()).thenReturn(
            createShippingOrdersWithShuffledQueuedBunkeringOperations()
        );

        // Act
        sut.processQueuedBunkeringOperations();

        // Assert: Only 3 more allowed today -> verify 3 updates performed
        final ArgumentCaptor<ShippingOrder> orderCaptor = ArgumentCaptor.forClass(ShippingOrder.class);
        verify(updateShippingOrderPort, times(3)).updateShippingOrder(orderCaptor.capture());

        final List<ShippingOrder> processedOrders = orderCaptor.getAllValues();
        assertEquals(3, processedOrders.size());

        // Verify each was correctly marked as completed
        for (ShippingOrder order : processedOrders) {
            assertTrue(order.isShipBunkered());
            assertTrue(order.getBunkering().isCompleted());
        }
        final List<ShippingOrderId> processedIds = processedOrders.stream().map(ShippingOrder::getShippingOrderId).toList();
        assertEquals(
            List.of(SHIPPING_ORDER_ID_1, SHIPPING_ORDER_ID_2, SHIPPING_ORDER_ID_3),
            processedIds
        );
    }

    // Mock DB result deliberately shuffled to simulate a non-FIFO list
    // (even though the real repository would return FIFO)
    private static List<ShippingOrder> createShippingOrdersWithShuffledQueuedBunkeringOperations() {
        final LocalDateTime startOfDay = TestIds.PROCESSING_DATE.atStartOfDay();
        return List.of(
            createShippingOrderWithQueuedBunkering(SHIPPING_ORDER_ID_1, startOfDay.minusMinutes(1)),
            createShippingOrderWithQueuedBunkering(SHIPPING_ORDER_ID_2, startOfDay.minusMinutes(4)),
            createShippingOrderWithQueuedBunkering(SHIPPING_ORDER_ID_3, startOfDay.minusMinutes(2)),
            createShippingOrderWithQueuedBunkering(SHIPPING_ORDER_ID_4, startOfDay.minusMinutes(3))
        );
    }

    private static ShippingOrder createShippingOrderWithQueuedBunkering(final ShippingOrderId id, final LocalDateTime queuedAt) {
        final LocalDateTime startOfDay = TestIds.PROCESSING_DATE.atStartOfDay();
        return new ShippingOrder(
            id,
            ReferenceId.newId(),
            new VesselNumber("VES-" + id.id().toString().substring(0, 4).toUpperCase()),
            TestIds.BUYER_ID,
            startOfDay.minusWeeks(1),
            startOfDay.plusWeeks(1),
            startOfDay.minusDays(1),
            null,
            ShippingOrderStatus.SHIP_DOCKED,
            null, // We don't care about the Inspection
            createQueuedBunkeringOperation(queuedAt)
        );
    }

    private static BunkeringOperation createQueuedBunkeringOperation(final LocalDateTime queuedAt) {
        return new BunkeringOperation(
            BunkeringOperationId.of(UUID.randomUUID()),
            queuedAt,
            null,
            BunkeringOperationStatus.QUEUED
        );
    }
}