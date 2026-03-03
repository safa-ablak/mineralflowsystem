package be.kdg.prog6.waterside.adapter.in.listener;

import be.kdg.prog6.common.event.warehousing.validation.ReferencedPurchaseOrderRejectedEvent;
import be.kdg.prog6.common.event.warehousing.validation.ReferencedPurchaseOrderValidatedEvent;
import be.kdg.prog6.waterside.domain.ReferenceId;
import be.kdg.prog6.waterside.port.in.command.MatchShippingOrderWithPurchaseOrderCommand;
import be.kdg.prog6.waterside.port.in.usecase.MatchShippingOrderWithPurchaseOrderUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.WATERSIDE;
import static be.kdg.prog6.waterside.adapter.config.WatersideMessagingTopology.REFERENCED_PURCHASE_ORDER_REJECTED_QUEUE;
import static be.kdg.prog6.waterside.adapter.config.WatersideMessagingTopology.REFERENCED_PURCHASE_ORDER_VALIDATED_QUEUE;

/**
 * Listens for events related to referenced Purchase Order validation and updates the Shipping Order state accordingly.
 */
@Component
public class ReferencedPurchaseOrderValidationListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferencedPurchaseOrderValidationListener.class);

    private final MatchShippingOrderWithPurchaseOrderUseCase matchShippingOrderWithPurchaseOrderUseCase;

    public ReferencedPurchaseOrderValidationListener(final MatchShippingOrderWithPurchaseOrderUseCase matchShippingOrderWithPurchaseOrderUseCase) {
        this.matchShippingOrderWithPurchaseOrderUseCase = matchShippingOrderWithPurchaseOrderUseCase;
    }

    // Happy path: ReferenceId of the SO matches PurchaseOrderId of the PO & BuyerId of the SO matches BuyerId of the PO
    @RabbitListener(queues = REFERENCED_PURCHASE_ORDER_VALIDATED_QUEUE)
    public void onReferencedPurchaseOrderValidated(final ReferencedPurchaseOrderValidatedEvent event) {
        LOGGER.info(
            "Received {} at {}",
            event.getClass().getSimpleName(),
            WATERSIDE
        );
        final MatchShippingOrderWithPurchaseOrderCommand command = new MatchShippingOrderWithPurchaseOrderCommand(
            ReferenceId.of(event.purchaseOrderId())
        );
        matchShippingOrderWithPurchaseOrderUseCase.matchShippingOrderWithPurchaseOrder(command);
    }

    // Referenced PO rejected: Log it in the console
    @RabbitListener(queues = REFERENCED_PURCHASE_ORDER_REJECTED_QUEUE)
    public void onReferencedPurchaseOrderRejected(final ReferencedPurchaseOrderRejectedEvent event) {
        LOGGER.warn(
            "Received {} at {}, reason: {}",
            event.getClass().getSimpleName(),
            WATERSIDE,
            event.reason()
        );
    }
}
