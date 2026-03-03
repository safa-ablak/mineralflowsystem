package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.ShippingOrderId;
import be.kdg.prog6.waterside.domain.exception.shippingorder.ShippingOrderNotFoundException;
import be.kdg.prog6.waterside.port.in.command.InitiateShippingOrderLoadingCommand;
import be.kdg.prog6.waterside.port.in.usecase.InitiateShippingOrderLoadingUseCase;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import be.kdg.prog6.waterside.port.out.ShippingOrderLoadingInitiatedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InitiateShippingOrderLoadingUseCaseImpl implements InitiateShippingOrderLoadingUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitiateShippingOrderLoadingUseCaseImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;
    private final ShippingOrderLoadingInitiatedPort shippingOrderLoadingInitiatedPort;

    public InitiateShippingOrderLoadingUseCaseImpl(final LoadShippingOrderPort loadShippingOrderPort,
                                                   final ShippingOrderLoadingInitiatedPort shippingOrderLoadingInitiatedPort) {
        this.loadShippingOrderPort = loadShippingOrderPort;
        this.shippingOrderLoadingInitiatedPort = shippingOrderLoadingInitiatedPort;
    }

    @Override
    public ShippingOrder initiateShippingOrderLoading(final InitiateShippingOrderLoadingCommand command) {
        final ShippingOrderId id = command.shippingOrderId();
        LOGGER.info("Initiating the loading for Shipping Order with ID {}", id.id());
        final ShippingOrder shippingOrder = loadShippingOrderPort.loadById(id).orElseThrow(
            () -> ShippingOrderNotFoundException.forId(id)
        );
        shippingOrder.ensureCanInitiateLoading();
        // Publish event that the shipping order is ready to be loaded (PO will be shipped in the Warehousing Ctx)
        shippingOrderLoadingInitiatedPort.shippingOrderLoadingInitiated(shippingOrder);
        return shippingOrder;
    }
}
