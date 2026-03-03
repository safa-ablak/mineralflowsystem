package be.kdg.prog6.landside;

import be.kdg.prog6.landside.core.WarehouseAvailabilityProjectorImpl;
import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.Warehouse;
import be.kdg.prog6.landside.port.in.command.ProjectAvailabilityCommand;
import be.kdg.prog6.landside.port.out.LoadWarehousePort;
import be.kdg.prog6.landside.port.out.UpdateWarehousePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WarehouseAvailabilityProjectorImplMockingTest {
    private WarehouseAvailabilityProjectorImpl sut;
    private LoadWarehousePort loadWarehousePort;
    private UpdateWarehousePort updateWarehousePort;

    @BeforeEach
    void setUp() {
        loadWarehousePort = mock(LoadWarehousePort.class);
        updateWarehousePort = mock(UpdateWarehousePort.class);
        sut = new WarehouseAvailabilityProjectorImpl(
            loadWarehousePort,
            updateWarehousePort
        );
    }

    // Available warehouse should become unavailable at %80
    @Test
    void warehouseShouldBecomeUnavailableAtLimit() {
        // Arrange
        final ProjectAvailabilityCommand command = new ProjectAvailabilityCommand(
            TestIds.WAREHOUSE_ID,
            BigDecimal.valueOf(80)
        );
        when(loadWarehousePort.loadWarehouseById(TestIds.WAREHOUSE_ID)).thenReturn(Optional.of(
            createAvailableWarehouse()
        ));

        // Act
        sut.projectAvailability(command);

        // Assert
        final ArgumentCaptor<Warehouse> warehouseCaptor = ArgumentCaptor.forClass(Warehouse.class);
        verify(updateWarehousePort).updateWarehouse(warehouseCaptor.capture());
        assertFalse(warehouseCaptor.getValue().isAvailable());
    }

    // Warehouse should remain available < %80
    @Test
    void warehouseShouldStayAvailableWhileUnderLimit() {
        // Arrange
        final ProjectAvailabilityCommand command = new ProjectAvailabilityCommand(
            TestIds.WAREHOUSE_ID,
            BigDecimal.valueOf(79.9999)
        );
        when(loadWarehousePort.loadWarehouseById(TestIds.WAREHOUSE_ID)).thenReturn(Optional.of(
            createAvailableWarehouse()
        ));

        // Act
        sut.projectAvailability(command);

        // Assert
        final ArgumentCaptor<Warehouse> warehouseCaptor = ArgumentCaptor.forClass(Warehouse.class);
        verify(updateWarehousePort).updateWarehouse(warehouseCaptor.capture());
        assertTrue(warehouseCaptor.getValue().isAvailable());
    }

    private static Warehouse createAvailableWarehouse() {
        return new Warehouse(
            TestIds.WAREHOUSE_ID,
            true,
            RawMaterial.IRON_ORE,
            TestIds.SUPPLIER_ID
        );
    }
}