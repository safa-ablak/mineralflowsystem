package be.kdg.prog6.waterside;

import be.kdg.prog6.common.security.UserRole;
import be.kdg.prog6.waterside.core.BunkerShipUseCaseImpl;
import be.kdg.prog6.waterside.domain.*;
import be.kdg.prog6.waterside.domain.exception.bunkering.BunkeringLimitExceededException;
import be.kdg.prog6.waterside.port.in.command.BunkerShipCommand;
import be.kdg.prog6.waterside.port.out.BunkeringOperationQueryPort;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import be.kdg.prog6.waterside.port.out.UpdateShippingOrderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BunkerShipUseCaseImplMockingTest {
    private BunkerShipUseCaseImpl sut;
    private BunkeringOperationQueryPort bunkeringOperationQueryPort;
    private LoadShippingOrderPort loadShippingOrderPort;
    private UpdateShippingOrderPort updateShippingOrderPort;

    private static final Clock FIXED_CLOCK = Clock.fixed(
        TestIds.BUNKERING_DATE.atStartOfDay().toInstant(ZoneOffset.UTC),
        ZoneOffset.UTC
    );

    @BeforeEach
    void setUp() {
        bunkeringOperationQueryPort = mock(BunkeringOperationQueryPort.class);
        loadShippingOrderPort = mock(LoadShippingOrderPort.class);
        updateShippingOrderPort = mock(UpdateShippingOrderPort.class);
        sut = new BunkerShipUseCaseImpl(
            bunkeringOperationQueryPort,
            loadShippingOrderPort,
            updateShippingOrderPort,
            FIXED_CLOCK
        );
    }

    @Test
    void shouldNotPerformBunkeringWhenLimitIsExceededAsBunkeringOfficer() {
        // Arrange
        when(bunkeringOperationQueryPort.countPerformedBunkeringOperationsByDate(TestIds.BUNKERING_DATE)).thenReturn(
            6
        );
        when(loadShippingOrderPort.loadById(TestIds.SHIPPING_ORDER_ID_1)).thenReturn(Optional.of(
            createShippingOrderWithQueuedBunkering()
        ));
        final BunkerShipCommand command = new BunkerShipCommand(
            TestIds.SHIPPING_ORDER_ID_1,
            UserRole.BUNKERING_OFFICER
        );

        // Act
        final Executable action = () -> sut.bunkerShip(command);

        // Assert
        assertThrows(BunkeringLimitExceededException.class, action);
    }

    @Test
    void shouldPerformBunkeringEvenIfLimitExceededAsWarehouseManager() {
        // Arrange
        when(bunkeringOperationQueryPort.countPerformedBunkeringOperationsByDate(TestIds.BUNKERING_DATE)).thenReturn(
            6
        );
        final ShippingOrder order = createShippingOrderWithQueuedBunkering();
        when(loadShippingOrderPort.loadById(TestIds.SHIPPING_ORDER_ID_1)).thenReturn(
            Optional.of(order)
        );
        final BunkerShipCommand command = new BunkerShipCommand(
            TestIds.SHIPPING_ORDER_ID_1,
            UserRole.WAREHOUSE_MANAGER
        );

        // Act
        sut.bunkerShip(command);

        // Assert
        final ArgumentCaptor<ShippingOrder> orderCaptor = ArgumentCaptor.forClass(ShippingOrder.class);
        verify(updateShippingOrderPort).updateShippingOrder(orderCaptor.capture());
        assertEquals(order.getShippingOrderId(), orderCaptor.getValue().getShippingOrderId());
        assertTrue(orderCaptor.getValue().isShipBunkered());
        assertTrue(orderCaptor.getValue().getBunkering().isCompleted());
    }

    private static ShippingOrder createShippingOrderWithQueuedBunkering() {
        final LocalDateTime base = TestIds.BUNKERING_DATE.atTime(0, 0);
        return new ShippingOrder(
            TestIds.SHIPPING_ORDER_ID_1,
            ReferenceId.newId(),
            new VesselNumber("VES-" + TestIds.SHIPPING_ORDER_ID_1.id().toString().substring(0, 4).toUpperCase()),
            TestIds.BUYER_ID,
            base.minusWeeks(1),
            base.plusWeeks(1),
            base.minusDays(1),
            null,
            ShippingOrderStatus.SHIP_DOCKED,
            null,
            createQueuedBunkeringOperation(base)
        );
    }

    private static BunkeringOperation createQueuedBunkeringOperation(final LocalDateTime base) {
        return new BunkeringOperation(
            BunkeringOperationId.of(UUID.randomUUID()),
            base.minusDays(1),
            null,
            BunkeringOperationStatus.QUEUED
        );
    }
}