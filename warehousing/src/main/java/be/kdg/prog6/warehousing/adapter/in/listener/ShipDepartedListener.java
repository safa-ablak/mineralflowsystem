package be.kdg.prog6.warehousing.adapter.in.listener;

import be.kdg.prog6.common.event.waterside.ShipDepartedEvent;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;
import be.kdg.prog6.warehousing.port.in.command.FulfillPurchaseOrderCommand;
import be.kdg.prog6.warehousing.port.in.usecase.FulfillPurchaseOrderUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.WAREHOUSING;
import static be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology.DEPARTED_SHIPS_QUEUE;

@Component
public class ShipDepartedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShipDepartedListener.class);

    private final FulfillPurchaseOrderUseCase fulfillPurchaseOrderUseCase;

    public ShipDepartedListener(final FulfillPurchaseOrderUseCase fulfillPurchaseOrderUseCase) {
        this.fulfillPurchaseOrderUseCase = fulfillPurchaseOrderUseCase;
    }

    /// Listen to the `ShipDepartedEvent` event. And mark the PO as fulfilled.
    @RabbitListener(queues = DEPARTED_SHIPS_QUEUE)
    public void onShipDeparted(final ShipDepartedEvent event) {
        LOGGER.info(
            "Received {} at {} for a ship departed from the port",
            event.getClass().getSimpleName(),
            WAREHOUSING
        );
        final FulfillPurchaseOrderCommand command = new FulfillPurchaseOrderCommand(
            PurchaseOrderId.of(event.referenceId())
        );
        fulfillPurchaseOrderUseCase.fulfillPurchaseOrder(command);
    }
}
