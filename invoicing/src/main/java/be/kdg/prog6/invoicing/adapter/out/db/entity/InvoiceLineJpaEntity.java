package be.kdg.prog6.invoicing.adapter.out.db.entity;

import be.kdg.prog6.invoicing.adapter.out.db.value.MonetaryValueEmbeddable;
import be.kdg.prog6.invoicing.domain.InvoiceLineType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(catalog = "invoicing", name = "invoice_lines")
public class InvoiceLineJpaEntity {
    @Id
    @Column(name = "id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceJpaEntity invoice;

    @Embedded
    private MonetaryValueEmbeddable amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private InvoiceLineType type;

    public InvoiceLineJpaEntity() {}

    public InvoiceLineJpaEntity(final UUID id, final MonetaryValueEmbeddable amount, final InvoiceLineType type) {
        this.id = id;
        this.amount = amount;
        this.type = type;
    }

    public InvoiceJpaEntity getInvoice() {
        return invoice;
    }

    public void setInvoice(final InvoiceJpaEntity invoice) {
        this.invoice = invoice;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public MonetaryValueEmbeddable getAmount() {
        return amount;
    }

    public void setAmount(final MonetaryValueEmbeddable amount) {
        this.amount = amount;
    }

    public InvoiceLineType getType() {
        return type;
    }

    public void setType(final InvoiceLineType type) {
        this.type = type;
    }
}
