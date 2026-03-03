package be.kdg.prog6.invoicing;

import be.kdg.prog6.invoicing.domain.Invoice;
import be.kdg.prog6.invoicing.port.out.UpdateInvoicePort;

/**
 * Stub for UpdateInvoicePort.
 * Stores the last invoice that was updated so assertions can be made in tests.
 */
class UpdateInvoicePortStub implements UpdateInvoicePort {
    private Invoice invoice;

    @Override
    public void updateInvoice(final Invoice invoice) {
        this.invoice = invoice;
    }

    public Invoice getInvoice() {
        return invoice;
    }
}