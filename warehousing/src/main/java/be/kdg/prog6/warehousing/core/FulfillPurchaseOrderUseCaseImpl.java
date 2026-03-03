package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.exception.purchaseorder.PurchaseOrderNotFoundException;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;
import be.kdg.prog6.warehousing.port.in.command.FulfillPurchaseOrderCommand;
import be.kdg.prog6.warehousing.port.in.usecase.FulfillPurchaseOrderUseCase;
import be.kdg.prog6.warehousing.port.out.LoadPurchaseOrderPort;
import be.kdg.prog6.warehousing.port.out.PurchaseOrderFulfilledPort;
import be.kdg.prog6.warehousing.port.out.UpdatePurchaseOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FulfillPurchaseOrderUseCaseImpl implements FulfillPurchaseOrderUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(FulfillPurchaseOrderUseCaseImpl.class);

    private final LoadPurchaseOrderPort loadPurchaseOrderPort;
    private final UpdatePurchaseOrderPort updatePurchaseOrderPort;
    private final PurchaseOrderFulfilledPort purchaseOrderFulfilledPort;

    public FulfillPurchaseOrderUseCaseImpl(final LoadPurchaseOrderPort loadPurchaseOrderPort,
                                           final UpdatePurchaseOrderPort updatePurchaseOrderPort,
                                           final PurchaseOrderFulfilledPort purchaseOrderFulfilledPort) {
        this.loadPurchaseOrderPort = loadPurchaseOrderPort;
        this.updatePurchaseOrderPort = updatePurchaseOrderPort;
        this.purchaseOrderFulfilledPort = purchaseOrderFulfilledPort;
    }

    @Override
    @Transactional
    public void fulfillPurchaseOrder(final FulfillPurchaseOrderCommand command) {
        final PurchaseOrderId id = command.purchaseOrderId();
        LOGGER.info("Fulfilling Purchase Order with ID {}", id.id());

        final PurchaseOrder purchaseOrder = loadPurchaseOrderPort.loadPurchaseOrderById(id).orElseThrow(
            () -> PurchaseOrderNotFoundException.forId(id)
        );
        purchaseOrder.fulfill(); // Fulfill the PO

        // Persist the updated Purchase Order + publish fulfillment event
        updatePurchaseOrderPort.updatePurchaseOrder(purchaseOrder);
        purchaseOrderFulfilledPort.purchaseOrderFulfilled(purchaseOrder);

        LOGGER.info("Purchase Order with ID {} has been fulfilled", id.id());
    }
}
