package be.kdg.prog6.warehousing.adapter.in.listener;

import be.kdg.prog6.common.event.waterside.ShippingOrderLoadingInitiatedEvent;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;
import be.kdg.prog6.warehousing.port.in.command.ShipPurchaseOrderCommand;
import be.kdg.prog6.warehousing.port.in.usecase.ShipPurchaseOrderUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.WAREHOUSING;
import static be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology.LOADING_INITIATED_SHIPPING_ORDERS_QUEUE;

@Component
public class ShippingOrderLoadingInitiatedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingOrderLoadingInitiatedListener.class);

    private final ShipPurchaseOrderUseCase shipPurchaseOrderUseCase;

    public ShippingOrderLoadingInitiatedListener(final ShipPurchaseOrderUseCase shipPurchaseOrderUseCase) {
        this.shipPurchaseOrderUseCase = shipPurchaseOrderUseCase;
    }

    /// Listen to the `ShippingOrderLoadingInitiatedEvent` event and ship the Purchase Order
    @RabbitListener(queues = LOADING_INITIATED_SHIPPING_ORDERS_QUEUE)
    public void onShippingOrderLoadingInitiated(final ShippingOrderLoadingInitiatedEvent event) {
        LOGGER.info(
            "Received {} at {} for a ship which is to be loaded",
            event.getClass().getSimpleName(),
            WAREHOUSING
        );
        final ShipPurchaseOrderCommand command = new ShipPurchaseOrderCommand(
            PurchaseOrderId.of(event.referenceId())
        );
        shipPurchaseOrderUseCase.shipPurchaseOrder(command);
    }
}
