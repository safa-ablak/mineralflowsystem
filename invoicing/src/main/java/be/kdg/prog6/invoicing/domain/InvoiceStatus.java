package be.kdg.prog6.invoicing.domain;

public enum InvoiceStatus {
    DRAFT, // When it is drafted, but hasn't been sent to the customer yet
    SENT // When it is sent to the customer
}
