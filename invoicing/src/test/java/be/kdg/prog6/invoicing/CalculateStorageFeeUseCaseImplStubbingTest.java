package be.kdg.prog6.invoicing;

import be.kdg.prog6.common.event.warehousing.StorageEntry;
import be.kdg.prog6.common.event.warehousing.WarehouseStorageReport;
import be.kdg.prog6.invoicing.core.CalculateStorageFeeUseCaseImpl;
import be.kdg.prog6.invoicing.domain.Invoice;
import be.kdg.prog6.invoicing.domain.Money;
import be.kdg.prog6.invoicing.port.in.command.CalculateStorageFeeCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateStorageFeeUseCaseImplStubbingTest {
    private CalculateStorageFeeUseCaseImpl sut;
    private UpdateInvoicePortStub updateInvoicePort;

    @BeforeEach
    void setUp() {
        updateInvoicePort = new UpdateInvoicePortStub();
        // Provide stubbed ports to isolate domain logic
        sut = new CalculateStorageFeeUseCaseImpl(
            new LoadRawMaterialPortStub(),
            new LoadInvoicePortStub(),
            updateInvoicePort
        );
    }

    @Test
    void shouldCalculateStorageFeeAndAddItToInvoice() {
        // Arrange
        final List<StorageEntry> storageEntries = List.of(
            new StorageEntry(BigDecimal.valueOf(10), 4) // 10 tons stored for 4 days
        );
        final WarehouseStorageReport report = new WarehouseStorageReport(
            UUID.randomUUID(), // We are not interested in the Warehouse in the Invoicing BC
            TestIds.RAW_MATERIAL_NAME.toLowerCase(),
            storageEntries
        );
        final CalculateStorageFeeCommand command = new CalculateStorageFeeCommand(
            TestIds.CUSTOMER_ID,
            List.of(report)
        );

        // Act
        sut.calculateStorageFee(command);

        // Assert
        final Invoice updatedInvoice = updateInvoicePort.getInvoice();
        assertEquals(1, updatedInvoice.getInvoiceLines().size());
        assertEquals(Money.of(200), updatedInvoice.calculateTotalAmount()); // 10 * 4 * $5
    }
}
