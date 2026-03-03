package be.kdg.prog6.invoicing;

import be.kdg.prog6.common.config.testsupport.TestTimeConfig;
import be.kdg.prog6.common.security.testsupport.TestSecurityConfig;
import be.kdg.prog6.common.security.testsupport.WithMockJwt;
import be.kdg.prog6.invoicing.adapter.out.db.entity.InvoiceJpaEntity;
import be.kdg.prog6.invoicing.adapter.out.db.entity.InvoiceLineJpaEntity;
import be.kdg.prog6.invoicing.adapter.out.db.repository.InvoiceJpaRepository;
import be.kdg.prog6.invoicing.adapter.out.db.value.MonetaryValueEmbeddable;
import be.kdg.prog6.invoicing.domain.InvoiceLineType;
import be.kdg.prog6.invoicing.domain.InvoiceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import({TestSecurityConfig.class, TestTimeConfig.class})
public class InvoiceCustomerControllerIntegrationTest extends AbstractDatabaseTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InvoiceJpaRepository invoiceJpaRepository;

    @BeforeEach
    void setUp() {
        invoiceJpaRepository.deleteAll();

        // Insert a draft invoice and its lines
        final InvoiceJpaEntity invoice = new InvoiceJpaEntity();
        invoice.setInvoiceId(UUID.randomUUID());
        invoice.setCustomerId(TestIds.CUSTOMER_ID.id());
        invoice.setDraftedDate(TestIds.INVOICE_DRAFTED_DATE);
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.addInvoiceLine(createInvoiceLine(150.00, InvoiceLineType.STORAGE_COST));
        invoice.addInvoiceLine(createInvoiceLine(50.00, InvoiceLineType.STORAGE_COST));
        invoice.addInvoiceLine(createInvoiceLine(20.00, InvoiceLineType.COMMISSION));
        invoiceJpaRepository.save(invoice);
    }

    private static InvoiceLineJpaEntity createInvoiceLine(final double amount, final InvoiceLineType type) {
        return new InvoiceLineJpaEntity(
            UUID.randomUUID(),
            new MonetaryValueEmbeddable(BigDecimal.valueOf(amount), "USD"),
            type
        );
    }

    @Test
    @WithMockJwt(role = "ACCOUNTANT")
    void shouldInvoiceCustomer() throws Exception {
        // Arrange
        final UUID customerId = TestIds.CUSTOMER_ID.id();

        // Act
        final ResultActions result = mockMvc.perform(post("/invoice-customer/{customer}", customerId)
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.customerId").value(customerId.toString())) // Verify that the customer ID matches
            .andExpect(jsonPath("$.draftedDate").value(TestIds.INVOICE_DRAFTED_DATE.toString())) // Verify that the drafted date matches the test data
            .andExpect(jsonPath("$.sentDate").value(TestTimeConfig.FIXED_NOW.toLocalDate().toString())) // Verify that the sent date matches the fixed clock
            .andExpect(jsonPath("$.status").value("SENT")) // Verify that the status is updated to SENT
            .andExpect(jsonPath("$.invoiceLines.length()").value(3)) // Verify that the invoice has 3 lines
            .andExpect(jsonPath("$.total.currencyCode").value("USD")) // Verify currency (USD)
            .andExpect(jsonPath("$.total.amount").value(220.00)); // Verify that the total amount is correct
    }
}