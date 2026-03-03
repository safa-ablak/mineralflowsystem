package be.kdg.prog6.invoicing;

import be.kdg.prog6.invoicing.core.CalculateCommissionFeeUseCaseImpl;
import be.kdg.prog6.invoicing.domain.*;
import be.kdg.prog6.invoicing.port.in.command.CalculateCommissionFeeCommand;
import be.kdg.prog6.invoicing.port.out.LoadInvoicePort;
import be.kdg.prog6.invoicing.port.out.LoadRawMaterialPort;
import be.kdg.prog6.invoicing.port.out.LoadYearlyCommissionRatePort;
import be.kdg.prog6.invoicing.port.out.UpdateInvoicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CalculateCommissionFeeUseCaseImplMockingTest {
    private CalculateCommissionFeeUseCaseImpl sut;
    private UpdateInvoicePort updateInvoicePort; // Verified, so kept as class attribute

    @BeforeEach
    void setUp() {
        final LoadInvoicePort loadInvoicePort = mock(LoadInvoicePort.class);
        final LoadRawMaterialPort loadRawMaterialPort = mock(LoadRawMaterialPort.class);
        final LoadYearlyCommissionRatePort loadYearlyCommissionRatePort = mock(LoadYearlyCommissionRatePort.class);
        updateInvoicePort = mock(UpdateInvoicePort.class);
        when(loadInvoicePort.loadDraftInvoiceByCustomerId(TestIds.CUSTOMER_ID)).thenReturn(
            createEmptyInvoice()
        );
        when(loadRawMaterialPort.loadRawMaterialByName(TestIds.RAW_MATERIAL_NAME)).thenReturn(Optional.of(
            createRawMaterial()
        ));
        when(loadYearlyCommissionRatePort.loadCurrentYearRate()).thenReturn(
            createCommissionRate()
        );
        sut = new CalculateCommissionFeeUseCaseImpl(
            loadRawMaterialPort,
            loadInvoicePort,
            loadYearlyCommissionRatePort,
            updateInvoicePort
        );
    }

    @Test
    void shouldAddCommissionFee() {
        // Arrange
        final CalculateCommissionFeeCommand command = new CalculateCommissionFeeCommand(
            TestIds.CUSTOMER_ID,
            Map.of(TestIds.RAW_MATERIAL_NAME, BigDecimal.valueOf(10)) // 10 tons of Iron Ore
        );

        // Act
        sut.calculateCommissionFee(command);

        // Assert
        final ArgumentCaptor<Invoice> invoiceCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(updateInvoicePort).updateInvoice(invoiceCaptor.capture());

        final Invoice updatedInvoice = invoiceCaptor.getValue();
        assertEquals(1, updatedInvoice.getInvoiceLines().size());
        assertEquals(InvoiceLineType.COMMISSION, updatedInvoice.getInvoiceLines().getFirst().getType());
        assertEquals(Money.of(22), updatedInvoice.calculateTotalAmount()); // 10 tons * $110/ton * 2% commission
    }

    private static Invoice createEmptyInvoice() {
        return new Invoice(TestIds.CUSTOMER_ID);
    }

    private static RawMaterial createRawMaterial() {
        return new RawMaterial(
            TestIds.RAW_MATERIAL_ID,
            TestIds.RAW_MATERIAL_NAME,
            Money.of(10), // $10/ton (not used here)
            Money.of(110) // $110/ton
        );
    }

    private static YearlyCommissionRate createCommissionRate() {
        return YearlyCommissionRate.of(2026, BigDecimal.valueOf(0.02)); // 2% commission rate for 2026
    }
}