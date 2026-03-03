package be.kdg.prog6.invoicing.adapter.in.listener;

import be.kdg.prog6.common.event.warehousing.PurchaseOrderFulfilledEvent;
import be.kdg.prog6.invoicing.domain.CustomerId;
import be.kdg.prog6.invoicing.port.in.command.CalculateCommissionFeeCommand;
import be.kdg.prog6.invoicing.port.in.usecase.CalculateCommissionFeeUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.INVOICING;
import static be.kdg.prog6.invoicing.adapter.config.InvoicingMessagingTopology.FULFILLED_PURCHASE_ORDERS_QUEUE;

@Component
public class PurchaseOrderFulfilledListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderFulfilledListener.class);

    private final CalculateCommissionFeeUseCase calculateCommissionFeeUseCase;

    public PurchaseOrderFulfilledListener(final CalculateCommissionFeeUseCase calculateCommissionFeeUseCase) {
        this.calculateCommissionFeeUseCase = calculateCommissionFeeUseCase;
    }

    /// Listen to the `PurchaseOrderFulfilledEvent` event and calculate the commission fee for the PO
    @RabbitListener(queues = FULFILLED_PURCHASE_ORDERS_QUEUE)
    public void onPurchaseOrderFulfilled(final PurchaseOrderFulfilledEvent event) {
        final CustomerId customerId = CustomerId.of(event.sellerId()); // Context Mapping: Seller is our Customer
        LOGGER.info(
            "Received {} at {} for a fulfilled Purchase Order for Customer {}",
            event.getClass().getSimpleName(),
            INVOICING,
            customerId.id()
        );
        final CalculateCommissionFeeCommand command = new CalculateCommissionFeeCommand(
            customerId,
            event.rawMaterialToAmount()
        );
        calculateCommissionFeeUseCase.calculateCommissionFee(command);
    }
}
