package be.kdg.prog6.invoicing.adapter.out.db.entity;

import be.kdg.prog6.invoicing.domain.InvoiceStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(catalog = "invoicing", name = "invoices")
public class InvoiceJpaEntity {
    @Id
    @Column(name = "invoice_id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID invoiceId;

    @Column(name = "customer_id", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID customerId;

    @Column(name = "drafted_date")
    private LocalDate draftedDate;

    @Column(name = "sent_date")
    private LocalDate sentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceLineJpaEntity> invoiceLines = new ArrayList<>();

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(final UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final UUID customerId) {
        this.customerId = customerId;
    }

    public LocalDate getDraftedDate() {
        return draftedDate;
    }

    public void setDraftedDate(final LocalDate draftedDate) {
        this.draftedDate = draftedDate;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }

    public void setSentDate(final LocalDate sentDate) {
        this.sentDate = sentDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(final InvoiceStatus status) {
        this.status = status;
    }

    public List<InvoiceLineJpaEntity> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(final List<InvoiceLineJpaEntity> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public void addInvoiceLine(final InvoiceLineJpaEntity line) {
        line.setInvoice(this);
        this.invoiceLines.add(line);
    }
}
