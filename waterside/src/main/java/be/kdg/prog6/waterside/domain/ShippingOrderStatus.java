package be.kdg.prog6.waterside.domain;

public enum ShippingOrderStatus {
    /**
     * The Shipping Order has been entered, but the referenced Purchase Order is yet to be validated.
     */
    PENDING_VALIDATION,
    /**
     * The Shipping Order has been successfully matched with a valid Purchase Order.
     */
    MATCHED,
    /**
     * Indicates that the vessel assigned to the shipping order has been docked at the port
     */
    SHIP_DOCKED,
    /**
     * Indicates that the vessel assigned to the shipping order has completed the mandatory refueling (bunkering) operation.
     * This step ensures the vessel is adequately fueled before departure.
     */
    SHIP_BUNKERED,
    /**
     * Indicates that the vessel assigned to the shipping order has completed the mandatory inspection operation.
     * This step ensures the vessel meets safety and regulatory standards before departure.
     */
    SHIP_INSPECTED,
    /**
     * The ship carrying the shipping order has been loaded and left the port.
     *
     */
    SHIP_DEPARTED
}
