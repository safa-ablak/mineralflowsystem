package be.kdg.prog6.invoicing;

import be.kdg.prog6.invoicing.domain.*;
import be.kdg.prog6.invoicing.port.out.LoadInvoicePort;

import java.util.List;

/**
 * Always returns a new empty draft invoice for the given customer.
 */
class LoadInvoicePortStub implements LoadInvoicePort {
    @Override
    public Invoice loadDraftInvoiceByCustomerId(final CustomerId customerId) {
        return new Invoice(customerId);
    }

    @Override
    public List<Invoice> loadInvoicesByStatus(final InvoiceStatus status) {
        return List.of(
            new Invoice(
                TestIds.INVOICE_ID,
                TestIds.CUSTOMER_ID,
                TestIds.INVOICE_DRAFTED_DATE,
                TestIds.INVOICE_SENT_DATE,
                status,
                List.of(new InvoiceLine(TestIds.INVOICE_LINE_1_ID, Money.of(10), InvoiceLineType.STORAGE_COST))
            )
        );
    }
}
