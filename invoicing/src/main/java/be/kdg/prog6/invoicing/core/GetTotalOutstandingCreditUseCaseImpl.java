package be.kdg.prog6.invoicing.core;

import be.kdg.prog6.invoicing.domain.Invoice;
import be.kdg.prog6.invoicing.domain.InvoiceStatus;
import be.kdg.prog6.invoicing.domain.Money;
import be.kdg.prog6.invoicing.port.in.usecase.query.GetTotalOutstandingCreditUseCase;
import be.kdg.prog6.invoicing.port.out.LoadInvoicePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static be.kdg.prog6.common.BoundedContext.INVOICING;
import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetTotalOutstandingCreditUseCaseImpl implements GetTotalOutstandingCreditUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetTotalOutstandingCreditUseCaseImpl.class);

    private final LoadInvoicePort loadInvoicePort;

    public GetTotalOutstandingCreditUseCaseImpl(final LoadInvoicePort loadInvoicePort) {
        this.loadInvoicePort = loadInvoicePort;
    }

    @Override
    @Transactional
    public Money getTotalOutstandingCredit() {
        LOGGER.info("Calculating total outstanding credit at {} ({})", KDG, INVOICING);
        final List<Invoice> sentInvoices = loadInvoicePort.loadInvoicesByStatus(InvoiceStatus.SENT);
        final Money total = sentInvoices.stream()
            .map(Invoice::calculateTotalAmount)
            .reduce(Money::add)
            .orElse(Money.of(0));
        LOGGER.info("Total outstanding credit: {} across {} sent invoices", total, sentInvoices.size());
        return total;
    }
}
