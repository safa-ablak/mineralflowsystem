package be.kdg.prog6.warehousing.port.in.usecase;

import be.kdg.prog6.warehousing.port.in.command.ValidateReferencedPurchaseOrderCommand;

@FunctionalInterface
public interface ValidateReferencedPurchaseOrderUseCase {
    void validateReferencedPurchaseOrder(ValidateReferencedPurchaseOrderCommand command);
}
