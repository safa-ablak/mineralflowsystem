package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.ShippingOrderId;
import be.kdg.prog6.waterside.domain.exception.shippingorder.ShippingOrderNotFoundException;
import be.kdg.prog6.waterside.port.in.command.RequestShippingOrderCorrectionCommand;
import be.kdg.prog6.waterside.port.in.usecase.RequestShippingOrderCorrectionUseCase;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import be.kdg.prog6.waterside.port.out.ShippingOrderCorrectionRequestedPort;
import be.kdg.prog6.waterside.port.out.UpdateShippingOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RequestShippingOrderCorrectionUseCaseImpl implements RequestShippingOrderCorrectionUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestShippingOrderCorrectionUseCaseImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;
    private final UpdateShippingOrderPort updateShippingOrderPort;
    private final ShippingOrderCorrectionRequestedPort shippingOrderCorrectionRequestedPort;

    public RequestShippingOrderCorrectionUseCaseImpl(final LoadShippingOrderPort loadShippingOrderPort,
                                                     final UpdateShippingOrderPort updateShippingOrderPort,
                                                     final ShippingOrderCorrectionRequestedPort shippingOrderCorrectionRequestedPort) {
        this.loadShippingOrderPort = loadShippingOrderPort;
        this.updateShippingOrderPort = updateShippingOrderPort;
        this.shippingOrderCorrectionRequestedPort = shippingOrderCorrectionRequestedPort;
    }

    @Override
    @Transactional
    public ShippingOrder requestShippingOrderCorrection(final RequestShippingOrderCorrectionCommand command) {
        final ShippingOrderId id = command.shippingOrderId();
        LOGGER.info("Requesting a correction for Shipping Order with ID {}", id.id());
        final ShippingOrder shippingOrder = loadShippingOrderPort.loadById(id).orElseThrow(
            () -> ShippingOrderNotFoundException.forId(id)
        );
        shippingOrder.amendDetails(command.buyerId(), command.referenceId());
        updateShippingOrderPort.updateShippingOrder(shippingOrder);
        shippingOrderCorrectionRequestedPort.shippingOrderCorrectionRequested(shippingOrder);
        return shippingOrder;
    }
}
