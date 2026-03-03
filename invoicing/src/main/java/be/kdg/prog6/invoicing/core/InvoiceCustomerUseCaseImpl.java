package be.kdg.prog6.invoicing.core;

import be.kdg.prog6.invoicing.domain.CustomerId;
import be.kdg.prog6.invoicing.domain.Invoice;
import be.kdg.prog6.invoicing.port.in.command.InvoiceCustomerCommand;
import be.kdg.prog6.invoicing.port.in.usecase.InvoiceCustomerUseCase;
import be.kdg.prog6.invoicing.port.out.LoadInvoicePort;
import be.kdg.prog6.invoicing.port.out.UpdateInvoicePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class InvoiceCustomerUseCaseImpl implements InvoiceCustomerUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceCustomerUseCaseImpl.class);

    private final LoadInvoicePort loadInvoicePort;
    private final UpdateInvoicePort updateInvoicePort;
    private final Clock clock;

    public InvoiceCustomerUseCaseImpl(final LoadInvoicePort loadInvoicePort,
                                      final UpdateInvoicePort updateInvoicePort,
                                      final Clock clock) {
        this.loadInvoicePort = loadInvoicePort;
        this.updateInvoicePort = updateInvoicePort;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Invoice invoiceCustomer(final InvoiceCustomerCommand command) {
        final CustomerId customerId = command.customerId();
        LOGGER.info("Invoicing the Customer with ID {}", customerId.id());
        final Invoice invoice = loadInvoicePort.loadDraftInvoiceByCustomerId(customerId);
        invoice.send(LocalDate.now(clock));
        updateInvoicePort.updateInvoice(invoice);
        LOGGER.info("Invoiced the Customer with ID {}\n\n{}", customerId.id(), invoice);
        return invoice;
    }
}
