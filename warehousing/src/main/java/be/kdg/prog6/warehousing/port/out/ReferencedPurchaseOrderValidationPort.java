package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.common.event.warehousing.validation.ReferencedPurchaseOrderRejectedEvent;
import be.kdg.prog6.common.event.warehousing.validation.ReferencedPurchaseOrderValidatedEvent;

public interface ReferencedPurchaseOrderValidationPort {
    void referencedPurchaseOrderValidated(ReferencedPurchaseOrderValidatedEvent event);

    void referencedPurchaseOrderRejected(ReferencedPurchaseOrderRejectedEvent event);
}