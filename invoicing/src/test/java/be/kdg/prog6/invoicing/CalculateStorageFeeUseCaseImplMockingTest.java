package be.kdg.prog6.invoicing;

import be.kdg.prog6.common.event.warehousing.StorageEntry;
import be.kdg.prog6.common.event.warehousing.WarehouseStorageReport;
import be.kdg.prog6.invoicing.core.CalculateStorageFeeUseCaseImpl;
import be.kdg.prog6.invoicing.domain.Invoice;
import be.kdg.prog6.invoicing.domain.InvoiceLineType;
import be.kdg.prog6.invoicing.domain.Money;
import be.kdg.prog6.invoicing.domain.RawMaterial;
import be.kdg.prog6.invoicing.port.in.command.CalculateStorageFeeCommand;
import be.kdg.prog6.invoicing.port.out.LoadInvoicePort;
import be.kdg.prog6.invoicing.port.out.LoadRawMaterialPort;
import be.kdg.prog6.invoicing.port.out.UpdateInvoicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CalculateStorageFeeUseCaseImplMockingTest {
    private CalculateStorageFeeUseCaseImpl sut;
    private UpdateInvoicePort updateInvoicePort; // Verified, so kept as class attribute

    @BeforeEach
    void setUp() {
        final LoadInvoicePort loadInvoicePort = mock(LoadInvoicePort.class);
        final LoadRawMaterialPort loadRawMaterialPort = mock(LoadRawMaterialPort.class);
        updateInvoicePort = mock(UpdateInvoicePort.class);
        when(loadInvoicePort.loadDraftInvoiceByCustomerId(TestIds.CUSTOMER_ID)).thenReturn(
            createEmptyInvoice()
        );
        when(loadRawMaterialPort.loadRawMaterialByName(TestIds.RAW_MATERIAL_NAME)).thenReturn(Optional.of(
            createRawMaterial()
        ));
        sut = new CalculateStorageFeeUseCaseImpl(
            loadRawMaterialPort,
            loadInvoicePort,
            updateInvoicePort
        );
    }

    @Test
    void shouldAddStorageFee() {
        // Arrange
        final List<StorageEntry> storageEntries = List.of(
            new StorageEntry(BigDecimal.valueOf(10), 4),
            new StorageEntry(BigDecimal.valueOf(30), 2)
        );
        final WarehouseStorageReport report = new WarehouseStorageReport(
            UUID.randomUUID(), // We are not interested in the Warehouse in the Invoicing BC
            TestIds.RAW_MATERIAL_NAME,
            storageEntries
        );
        final CalculateStorageFeeCommand command = new CalculateStorageFeeCommand(
            TestIds.CUSTOMER_ID,
            List.of(report)
        );

        // Act
        sut.calculateStorageFee(command);

        // Assert
        final ArgumentCaptor<Invoice> invoiceCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(updateInvoicePort).updateInvoice(invoiceCaptor.capture());

        final Invoice updatedInvoice = invoiceCaptor.getValue();
        assertEquals(1, updatedInvoice.getInvoiceLines().size());
        assertEquals(InvoiceLineType.STORAGE_COST, updatedInvoice.getInvoiceLines().getFirst().getType());
        // (10 * 4 + 30 * 2) * ($5/ton/day) = $500
        assertEquals(Money.of(500), updatedInvoice.calculateTotalAmount());
    }

    private static Invoice createEmptyInvoice() {
        return new Invoice(TestIds.CUSTOMER_ID);
    }

    private static RawMaterial createRawMaterial() {
        return new RawMaterial(
            TestIds.RAW_MATERIAL_ID,
            TestIds.RAW_MATERIAL_NAME,
            Money.of(5), // $5/ton/day
            Money.of(110) // $110/ton (not used in storage fee)
        );
    }
}