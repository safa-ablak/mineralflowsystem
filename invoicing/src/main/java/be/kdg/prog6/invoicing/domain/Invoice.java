package be.kdg.prog6.invoicing.domain;

import be.kdg.prog6.common.event.warehousing.StorageEntry;
import be.kdg.prog6.invoicing.domain.exception.EmptyInvoiceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static be.kdg.prog6.common.ProjectInfo.KDG;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class Invoice {
    private final InvoiceId invoiceId;
    private final CustomerId customerId;
    private final LocalDate draftedDate;
    private LocalDate sentDate; // Set when invoice is sent to the Customer
    private InvoiceStatus status;
    private List<InvoiceLine> invoiceLines;

    public Invoice(final CustomerId customerId) {
        this.invoiceId = InvoiceId.newId();
        this.customerId = customerId;
        this.draftedDate = LocalDate.now();
        this.sentDate = null; // Not set until the invoice is sent
        this.status = InvoiceStatus.DRAFT; // Initial state
        this.invoiceLines = new ArrayList<>();
    }

    public Invoice(final InvoiceId invoiceId, final CustomerId customerId, final LocalDate draftedDate, final LocalDate sentDate, final InvoiceStatus status, final List<InvoiceLine> invoiceLines) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.draftedDate = draftedDate;
        this.sentDate = sentDate;
        this.status = status;
        this.invoiceLines = invoiceLines;
    }

    public void addInvoiceLine(final InvoiceLine line) {
        requireNonNull(line, "Invoice line cannot be null");
        invoiceLines.add(line);
    }

    public InvoiceLine addCommissionFee(final Money baseAmount, final BigDecimal commissionRate) {
        final InvoiceLine commissionFee = InvoiceLine.createCommissionFee(baseAmount, commissionRate);
        addInvoiceLine(commissionFee);
        return commissionFee;
    }

    public void addStorageCostFee(final RawMaterial rawMaterial, final List<StorageEntry> storageEntries) {
        BigDecimal total = BigDecimal.ZERO;

        for (StorageEntry entry : storageEntries) {
            // If the amount is non-positive OR days stored = 0, skip this entry
            if (!isChargeableForStorageCostFee(entry)) continue;

            final BigDecimal amount = entry.remainingAmount();
            final int daysStored = entry.daysStored();
            total = total.add(amount.multiply(BigDecimal.valueOf(daysStored)));
        }
        // If the total is zero, do not add a storage fee line
        if (total.compareTo(BigDecimal.ZERO) == 0) return;

        final InvoiceLine storageFee = InvoiceLine.createStorageCostFee(rawMaterial, total);
        addInvoiceLine(storageFee);
    }

    private static boolean isChargeableForStorageCostFee(final StorageEntry entry) {
        return entry.remainingAmount() != null
            && entry.remainingAmount().compareTo(BigDecimal.ZERO) > 0
            && entry.daysStored() > 0;
    }

    public void send(final LocalDate sentDate) {
        if (!isReadyToSend()) {
            throw EmptyInvoiceException.forCustomer(customerId);
        }
        setSentDate(sentDate);
        setStatus(InvoiceStatus.SENT);
    }

    private void setStatus(final InvoiceStatus status) {
        this.status = status;
    }

    public Money calculateTotalAmount() {
        return invoiceLines.stream()
            .map(InvoiceLine::getAmount)
            .reduce(Money::add)
            .orElse(Money.of(0));
    }

    public InvoiceId getInvoiceId() {
        return invoiceId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public LocalDate getDraftedDate() {
        return draftedDate;
    }

    private void setSentDate(final LocalDate sentDate) {
        this.sentDate = sentDate;
    }

    public List<InvoiceLine> getInvoiceLines() {
        return Collections.unmodifiableList(invoiceLines);
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }

    private void setInvoiceLines(final List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    private boolean isReadyToSend() {
        return !invoiceLines.isEmpty();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final Invoice that = (Invoice) other;
        return invoiceId.equals(that.invoiceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        // Might look off if the company name ever changes :)
        sb.append("\n┌=================== ").append(KDG).append(" INVOICE ===================┐\n");
        sb.append("Invoice ID:      ").append(invoiceId.id()).append("\n");
        sb.append("Customer ID:     ").append(customerId.id()).append("\n");
        sb.append("Status:          ").append(status).append("\n");
        sb.append("Drafted Date:    ").append(draftedDate).append("\n");
        if (sentDate != null) {
            sb.append("Sent Date:       ").append(sentDate).append("\n");
        } else {
            sb.append("Sent Date:       Not sent yet\n");
        }
        sb.append("\n------------------- Invoice Lines -------------------\n");
        if (invoiceLines.isEmpty()) {
            sb.append("No invoice lines to display.\n");
        } else {
            sb.append(format("%-20s %-15s\n", "Type", "Amount"));
            sb.append("-----------------------------------------------------\n");
            for (InvoiceLine line : invoiceLines) {
                final String type = line.getType().name();
                final Money lineAmount = line.getAmount();
                sb.append(format("%-20s %-15s\n", type, lineAmount));
            }
        }
        final Money totalAmount = calculateTotalAmount();
        sb.append("+= ==================================================\n");
        sb.append("\nTotal Amount:    ").append(totalAmount).append("\n");
        sb.append("└===================================================┘\n");
        return sb.toString();
    }
}
