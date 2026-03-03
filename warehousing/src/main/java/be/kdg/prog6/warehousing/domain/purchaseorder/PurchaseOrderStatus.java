package be.kdg.prog6.warehousing.domain.purchaseorder;

/**
 * Enum representing the status of a Purchase Order.
 */
public enum PurchaseOrderStatus {
    /**
     * The purchase order has been sent but not yet fulfilled.
     */
    PENDING,
    /**
     * The purchase order has been fully fulfilled and is complete.
     */
    FULFILLED
}
