package be.kdg.prog6.waterside.adapter.in.listener;

import be.kdg.prog6.common.event.warehousing.PurchaseOrderShippedEvent;
import be.kdg.prog6.waterside.domain.ReferenceId;
import be.kdg.prog6.waterside.port.in.command.DepartShipCommand;
import be.kdg.prog6.waterside.port.in.usecase.DepartShipUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.WATERSIDE;
import static be.kdg.prog6.waterside.adapter.config.WatersideMessagingTopology.SHIPPED_PURCHASE_ORDERS_QUEUE;

@Component
public class PurchaseOrderShippedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderShippedListener.class);

    private final DepartShipUseCase departShipUseCase;

    public PurchaseOrderShippedListener(final DepartShipUseCase departShipUseCase) {
        this.departShipUseCase = departShipUseCase;
    }

    /// Listen to the `PurchaseOrderShippedEvent` and publish a `ShipDepartedEvent`
    @RabbitListener(queues = SHIPPED_PURCHASE_ORDERS_QUEUE)
    public void onPurchaseOrderShipped(final PurchaseOrderShippedEvent event) {
        LOGGER.info(
            "Received {} at {} for a shipped Purchase Order",
            event.getClass().getSimpleName(),
            WATERSIDE
        );
        final DepartShipCommand command = new DepartShipCommand(
            ReferenceId.of(event.purchaseOrderId())
        );
        departShipUseCase.departShip(command);
    }
}
