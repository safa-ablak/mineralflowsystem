package be.kdg.prog6.warehousing.adapter.in.listener;

import be.kdg.prog6.common.event.waterside.ShippingOrderEnteredEvent;
import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;
import be.kdg.prog6.warehousing.port.in.command.ValidateReferencedPurchaseOrderCommand;
import be.kdg.prog6.warehousing.port.in.usecase.ValidateReferencedPurchaseOrderUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.WAREHOUSING;
import static be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology.ENTERED_SHIPPING_ORDERS_QUEUE;

@Component
public class ShippingOrderEnteredListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingOrderEnteredListener.class);

    private final ValidateReferencedPurchaseOrderUseCase validateReferencedPurchaseOrderUseCase;

    public ShippingOrderEnteredListener(final ValidateReferencedPurchaseOrderUseCase validateReferencedPurchaseOrderUseCase) {
        this.validateReferencedPurchaseOrderUseCase = validateReferencedPurchaseOrderUseCase;
    }

    @RabbitListener(queues = ENTERED_SHIPPING_ORDERS_QUEUE)
    public void onShippingOrderEntered(final ShippingOrderEnteredEvent event) {
        LOGGER.info(
            "Received {} for an entered Shipping Order at {}",
            event.getClass().getSimpleName(),
            WAREHOUSING
        );
        final ValidateReferencedPurchaseOrderCommand command = new ValidateReferencedPurchaseOrderCommand(
            BuyerId.of(event.buyerId()),
            PurchaseOrderId.of(event.referenceId()),
            event.shippingOrderId(),
            event.vesselNumber()
        );
        validateReferencedPurchaseOrderUseCase.validateReferencedPurchaseOrder(command);
    }
}
