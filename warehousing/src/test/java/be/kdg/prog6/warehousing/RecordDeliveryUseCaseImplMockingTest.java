package be.kdg.prog6.warehousing;

import be.kdg.prog6.warehousing.core.RecordDeliveryUseCaseImpl;
import be.kdg.prog6.warehousing.domain.storage.*;
import be.kdg.prog6.warehousing.port.in.command.RecordDeliveryCommand;
import be.kdg.prog6.warehousing.port.out.DeliveryRecordedPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecordDeliveryUseCaseImplMockingTest {
    private RecordDeliveryUseCaseImpl sut;
    private DeliveryRecordedPort deliveryRecordedPort;

    @BeforeEach
    void setUp() {
        deliveryRecordedPort = mock(DeliveryRecordedPort.class);
        final LoadWarehousePort loadWarehousePort = mock(LoadWarehousePort.class);
        when(loadWarehousePort.loadWarehouseById(TestIds.WAREHOUSE_ID_1)).thenReturn(Optional.of(
            createEmptyWarehouse()
        ));
        sut = new RecordDeliveryUseCaseImpl(
            loadWarehousePort,
            List.of(deliveryRecordedPort)
        );
    }

    private static Warehouse createEmptyWarehouse() {
        final WarehouseId warehouseId = TestIds.WAREHOUSE_ID_1;
        return new Warehouse(
            warehouseId,
            TestIds.SELLER_ID,
            TestIds.WAREHOUSE_NUMBER_1,
            TestIds.RAW_MATERIAL_1,
            Balance.ORIGIN,
            StockLedger.emptyFor(warehouseId),
            TestIds.DEFAULT_SITE_LOCATION
        );
    }

    @Test
    void shouldRecordDelivery() {
        // Act
        sut.recordDelivery(new RecordDeliveryCommand(TestIds.WAREHOUSE_ID_1, BigDecimal.valueOf(20)));

        // Assert
        final ArgumentCaptor<Warehouse> warehouseCaptor = ArgumentCaptor.forClass(Warehouse.class);
        final ArgumentCaptor<Delivery> deliveryCaptor = ArgumentCaptor.forClass(Delivery.class);
        verify(deliveryRecordedPort).deliveryRecorded(warehouseCaptor.capture(), deliveryCaptor.capture());

        assertEquals(BigDecimal.valueOf(20), warehouseCaptor.getValue().balance());
        assertEquals(TestIds.SELLER_ID, warehouseCaptor.getValue().getSellerId());
        assertEquals(BigDecimal.valueOf(20), deliveryCaptor.getValue().amount());
    }
}
