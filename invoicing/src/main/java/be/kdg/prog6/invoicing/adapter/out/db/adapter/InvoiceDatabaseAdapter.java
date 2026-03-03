package be.kdg.prog6.invoicing.adapter.out.db.adapter;

import be.kdg.prog6.invoicing.adapter.out.db.entity.InvoiceJpaEntity;
import be.kdg.prog6.invoicing.adapter.out.db.entity.InvoiceLineJpaEntity;
import be.kdg.prog6.invoicing.adapter.out.db.repository.InvoiceJpaRepository;
import be.kdg.prog6.invoicing.adapter.out.db.value.MonetaryValueEmbeddable;
import be.kdg.prog6.invoicing.domain.*;
import be.kdg.prog6.invoicing.port.out.LoadInvoicePort;
import be.kdg.prog6.invoicing.port.out.UpdateInvoicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceDatabaseAdapter implements LoadInvoicePort, UpdateInvoicePort {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceDatabaseAdapter.class);

    private final InvoiceJpaRepository invoiceJpaRepository;

    public InvoiceDatabaseAdapter(final InvoiceJpaRepository invoiceJpaRepository) {
        this.invoiceJpaRepository = invoiceJpaRepository;
    }

    // We expect 1 Draft Invoice per Customer to be present, if not exists create a new one.
    // Just like in the Grandparents BC of the 'piggybank-solution' project where Owner is assumed to be present,
    // Customer here is also assumed present in the Invoicing BC hence 'orElseGet()'
    @Override
    public Invoice loadDraftInvoiceByCustomerId(final CustomerId customerId) {
        LOGGER.info("Loading Draft Invoice for Customer ID {}", customerId.id());
        return invoiceJpaRepository.findByCustomerIdAndStatus(customerId.id(), InvoiceStatus.DRAFT)
            .map(this::toInvoice)
            .orElseGet(() -> new Invoice(customerId));
    }

    @Override
    public List<Invoice> loadInvoicesByStatus(final InvoiceStatus status) {
        LOGGER.info("Loading all {} Invoices", status);
        return invoiceJpaRepository.findByStatus(status)
            .stream()
            .map(this::toInvoice)
            .toList();
    }

    @Override
    public void updateInvoice(final Invoice invoice) {
        final InvoiceJpaEntity invoiceJpaEntity = toJpaInvoice(invoice);
        LOGGER.info("Updating Invoice with ID {}", invoice.getInvoiceId().id());
        invoiceJpaRepository.save(invoiceJpaEntity);
    }

    private InvoiceJpaEntity toJpaInvoice(final Invoice invoice) {
        final InvoiceJpaEntity invoiceJpaEntity = new InvoiceJpaEntity();
        invoiceJpaEntity.setInvoiceId(invoice.getInvoiceId().id());
        invoiceJpaEntity.setCustomerId(invoice.getCustomerId().id());
        invoiceJpaEntity.setDraftedDate(invoice.getDraftedDate());
        invoiceJpaEntity.setSentDate(invoice.getSentDate());
        invoiceJpaEntity.setStatus(invoice.getStatus());
        invoice.getInvoiceLines()
            .stream()
            .map(this::toJpaInvoiceLine)
            .forEach(invoiceJpaEntity::addInvoiceLine);
        return invoiceJpaEntity;
    }

    private InvoiceLineJpaEntity toJpaInvoiceLine(final InvoiceLine invoiceLine) {
        final InvoiceLineJpaEntity invoiceLineJpaEntity = new InvoiceLineJpaEntity();
        invoiceLineJpaEntity.setId(invoiceLine.getId().id());
        final MonetaryValueEmbeddable amount = toMonetaryValueEmbeddable(invoiceLine.getAmount());
        invoiceLineJpaEntity.setAmount(amount);
        invoiceLineJpaEntity.setType(invoiceLine.getType());
        return invoiceLineJpaEntity;
    }

    private Invoice toInvoice(final InvoiceJpaEntity invoiceJpaEntity) {
        final List<InvoiceLine> invoiceLines = invoiceJpaEntity.getInvoiceLines()
            .stream()
            .map(this::toInvoiceLine)
            .collect(Collectors.toList());
        return new Invoice(
            InvoiceId.of(invoiceJpaEntity.getInvoiceId()),
            CustomerId.of(invoiceJpaEntity.getCustomerId()),
            invoiceJpaEntity.getDraftedDate(),
            invoiceJpaEntity.getSentDate(),
            invoiceJpaEntity.getStatus(),
            invoiceLines
        );
    }

    private InvoiceLine toInvoiceLine(final InvoiceLineJpaEntity invoiceLineJpaEntity) {
        final Money amount = toMoney(invoiceLineJpaEntity.getAmount());
        return new InvoiceLine(
            InvoiceLineId.of(invoiceLineJpaEntity.getId()),
            amount,
            invoiceLineJpaEntity.getType()
        );
    }

    private static Money toMoney(final MonetaryValueEmbeddable embeddable) {
        return new Money(embeddable.getAmount(), Currency.getInstance(embeddable.getCurrency()));
    }

    private static MonetaryValueEmbeddable toMonetaryValueEmbeddable(final Money money) {
        return new MonetaryValueEmbeddable(
            money.getAmount(),
            money.getCurrency().getCurrencyCode()
        );
    }
}
