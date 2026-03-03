package be.kdg.prog6.invoicing.port.out;

import be.kdg.prog6.invoicing.domain.Invoice;

/**
 * Persists the given {@link Invoice}, either by creating a new record or updating an existing one.
 * The implementing adapter {@link be.kdg.prog6.invoicing.adapter.out.db.adapter.InvoiceDatabaseAdapter}
 * decides whether to insert or update based on the invoice's identity.
 */
@FunctionalInterface
public interface UpdateInvoicePort {
    void updateInvoice(Invoice invoice);
}
