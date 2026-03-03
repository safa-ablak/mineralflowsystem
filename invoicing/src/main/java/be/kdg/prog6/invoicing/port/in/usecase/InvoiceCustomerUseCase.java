package be.kdg.prog6.invoicing.port.in.usecase;

import be.kdg.prog6.invoicing.domain.Invoice;
import be.kdg.prog6.invoicing.port.in.command.InvoiceCustomerCommand;

/*
 * At request invoicing to customers (invoice outstanding credit)
 */
@FunctionalInterface
public interface InvoiceCustomerUseCase {
    Invoice invoiceCustomer(InvoiceCustomerCommand command);
}
