package be.kdg.prog6.invoicing.port.out;

import be.kdg.prog6.invoicing.domain.CustomerId;
import be.kdg.prog6.invoicing.domain.Invoice;
import be.kdg.prog6.invoicing.domain.InvoiceStatus;

import java.util.List;

public interface LoadInvoicePort {
    // 1 Draft per Customer
    Invoice loadDraftInvoiceByCustomerId(CustomerId customerId);

    List<Invoice> loadInvoicesByStatus(InvoiceStatus status);
}
