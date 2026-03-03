package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.ShippingOrderId;
import be.kdg.prog6.waterside.domain.exception.shippingorder.ShippingOrderNotFoundException;
import be.kdg.prog6.waterside.port.in.command.InspectShipCommand;
import be.kdg.prog6.waterside.port.in.usecase.InspectShipUseCase;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import be.kdg.prog6.waterside.port.out.UpdateShippingOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InspectShipUseCaseImpl implements InspectShipUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(InspectShipUseCaseImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;
    private final UpdateShippingOrderPort updateShippingOrderPort;

    public InspectShipUseCaseImpl(final LoadShippingOrderPort loadShippingOrderPort,
                                  final UpdateShippingOrderPort updateShippingOrderPort) {
        this.loadShippingOrderPort = loadShippingOrderPort;
        this.updateShippingOrderPort = updateShippingOrderPort;
    }

    @Override
    @Transactional
    public ShippingOrder inspectShip(final InspectShipCommand command) {
        final ShippingOrderId id = command.shippingOrderId();
        final String inspectorSignature = command.inspectorSignature();

        final ShippingOrder shippingOrder = loadShippingOrderPort.loadById(id).orElseThrow(
            () -> ShippingOrderNotFoundException.forId(id)
        );
        shippingOrder.performInspection(inspectorSignature);
        updateShippingOrderPort.updateShippingOrder(shippingOrder);

        LOGGER.info("Inspection Operation has been performed for Vessel: {}", shippingOrder.getVesselNumber());
        return shippingOrder;
    }
}
