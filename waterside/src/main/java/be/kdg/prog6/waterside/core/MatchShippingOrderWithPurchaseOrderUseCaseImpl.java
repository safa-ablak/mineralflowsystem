package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ReferenceId;
import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.exception.shippingorder.ShippingOrderNotFoundException;
import be.kdg.prog6.waterside.port.in.command.MatchShippingOrderWithPurchaseOrderCommand;
import be.kdg.prog6.waterside.port.in.usecase.MatchShippingOrderWithPurchaseOrderUseCase;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import be.kdg.prog6.waterside.port.out.UpdateShippingOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MatchShippingOrderWithPurchaseOrderUseCaseImpl implements MatchShippingOrderWithPurchaseOrderUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchShippingOrderWithPurchaseOrderUseCaseImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;
    private final UpdateShippingOrderPort updateShippingOrderPort;

    public MatchShippingOrderWithPurchaseOrderUseCaseImpl(final LoadShippingOrderPort loadShippingOrderPort,
                                                          final UpdateShippingOrderPort updateShippingOrderPort) {
        this.loadShippingOrderPort = loadShippingOrderPort;
        this.updateShippingOrderPort = updateShippingOrderPort;
    }

    @Override
    @Transactional
    public void matchShippingOrderWithPurchaseOrder(final MatchShippingOrderWithPurchaseOrderCommand command) {
        final ReferenceId referenceId = command.referenceId();
        LOGGER.info("Matching the Shipping Order with Reference ID {}", referenceId.id());

        final ShippingOrder shippingOrder = loadShippingOrderPort.loadByReferenceId(referenceId).orElseThrow(
            () -> ShippingOrderNotFoundException.forReferenceId(referenceId)
        );
        shippingOrder.match();

        LOGGER.info("Shipping Order with ID {} has been matched to Purchase Order with ID {}",
            shippingOrder.getShippingOrderId().id(), referenceId.id()
        );
        // Persist the updated shipping order via update port
        updateShippingOrderPort.updateShippingOrder(shippingOrder);
    }
}
