package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.common.event.warehousing.PurchaseOrderShippedEvent;

@FunctionalInterface
public interface PurchaseOrderShippedPort {
    void purchaseOrderShipped(PurchaseOrderShippedEvent event);
}
