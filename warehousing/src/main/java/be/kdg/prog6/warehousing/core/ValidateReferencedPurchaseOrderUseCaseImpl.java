package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.common.event.warehousing.validation.ReferencedPurchaseOrderRejectedEvent;
import be.kdg.prog6.common.event.warehousing.validation.ReferencedPurchaseOrderValidatedEvent;
import be.kdg.prog6.warehousing.adapter.out.publisher.ReferencedPurchaseOrderValidationPublisher;
import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderStatus;
import be.kdg.prog6.warehousing.port.in.command.ValidateReferencedPurchaseOrderCommand;
import be.kdg.prog6.warehousing.port.in.usecase.ValidateReferencedPurchaseOrderUseCase;
import be.kdg.prog6.warehousing.port.out.LoadPurchaseOrderPort;
import be.kdg.prog6.warehousing.port.out.UpdatePurchaseOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

/**
 * Validates that a referenced Purchase Order is valid for a Shipping Order:<br>
 * - PO must exist<br>
 * - Must belong to the Buyer<br>
 * - Must be in PENDING status<br>
 * <p>
 * Publishes a ReferencedPurchaseOrderValidatedEvent or ReferencedPurchaseOrderRejectedEvent.
 */
@Service
public class ValidateReferencedPurchaseOrderUseCaseImpl implements ValidateReferencedPurchaseOrderUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateReferencedPurchaseOrderUseCaseImpl.class);

    private final LoadPurchaseOrderPort loadPurchaseOrderPort;
    /// To fill in vesselNumber field on the PO, see `/docs/example/PO_Example.json` which was provided on Canvas
    private final UpdatePurchaseOrderPort updatePurchaseOrderPort;
    private final ReferencedPurchaseOrderValidationPublisher publisher;

    public ValidateReferencedPurchaseOrderUseCaseImpl(final LoadPurchaseOrderPort loadPurchaseOrderPort,
                                                      final UpdatePurchaseOrderPort updatePurchaseOrderPort,
                                                      final ReferencedPurchaseOrderValidationPublisher publisher) {
        this.loadPurchaseOrderPort = loadPurchaseOrderPort;
        this.updatePurchaseOrderPort = updatePurchaseOrderPort;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public void validateReferencedPurchaseOrder(final ValidateReferencedPurchaseOrderCommand command) {
        final BuyerId buyerId = command.buyerId();
        final UUID soId = command.shippingOrderId();
        final PurchaseOrderId poId = command.purchaseOrderId();
        final String vesselNumber = command.vesselNumber();

        final Optional<PurchaseOrder> optionalPurchaseOrder =
            loadPurchaseOrderPort.loadPurchaseOrderByBuyerIdAndIdAndStatus(
                buyerId,
                poId,
                PurchaseOrderStatus.PENDING
            );
        if (optionalPurchaseOrder.isPresent()) {
            // Fill in vesselNumber field on the PO (if provided)
            final PurchaseOrder purchaseOrder = optionalPurchaseOrder.get();
            if (vesselNumber != null) {
                purchaseOrder.fillVesselNumber(vesselNumber);
            }
            updatePurchaseOrderPort.updatePurchaseOrder(purchaseOrder);

            // Publish validation event
            final ReferencedPurchaseOrderValidatedEvent validationEvent = new ReferencedPurchaseOrderValidatedEvent(
                poId.id()
            );
            LOGGER.info("PO {} validated for SO {} -> publishing {}",
                poId, soId, validationEvent.getClass().getSimpleName()
            );
            publisher.referencedPurchaseOrderValidated(validationEvent);
        } else {
            // Publish rejection event
            final ReferencedPurchaseOrderRejectedEvent rejectionEvent = formatReferencedPurchaseOrderRejectedEvent(
                poId, soId, buyerId
            );
            LOGGER.warn("PO {} invalid for SO {} -> publishing {}",
                poId, soId, rejectionEvent.getClass().getSimpleName()
            );
            publisher.referencedPurchaseOrderRejected(rejectionEvent);
        }
    }

    private static ReferencedPurchaseOrderRejectedEvent formatReferencedPurchaseOrderRejectedEvent(
        final PurchaseOrderId poId, final UUID soId, final BuyerId buyerId
    ) {
        final String reason = format("""
            Referenced Purchase Order '%s' invalid for Shipping Order '%s'.
            Possible causes:
              - It does not exist.
              - It is already fulfilled.
              - It does not belong to the specified Buyer (ID: %s).
            """, poId.id(), soId, buyerId.id()
        );
        return new ReferencedPurchaseOrderRejectedEvent(poId.id(), reason);
    }
}
