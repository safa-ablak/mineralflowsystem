package be.kdg.prog6.invoicing.domain;

public enum InvoiceLineType {
    // Our Customers (Sellers) are only charged for commission and storage cost fees
    // Commission = %1 of Total Raw Material Cost / PO
    COMMISSION,
    STORAGE_COST
}