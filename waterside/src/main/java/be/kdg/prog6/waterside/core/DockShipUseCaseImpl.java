package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.ShippingOrderId;
import be.kdg.prog6.waterside.domain.exception.shippingorder.ShippingOrderNotFoundException;
import be.kdg.prog6.waterside.port.in.command.DockShipCommand;
import be.kdg.prog6.waterside.port.in.usecase.DockShipUseCase;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import be.kdg.prog6.waterside.port.out.UpdateShippingOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DockShipUseCaseImpl implements DockShipUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockShipUseCaseImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;
    private final UpdateShippingOrderPort updateShippingOrderPort;

    public DockShipUseCaseImpl(final LoadShippingOrderPort loadShippingOrderPort,
                               final UpdateShippingOrderPort updateShippingOrderPort) {
        this.loadShippingOrderPort = loadShippingOrderPort;
        this.updateShippingOrderPort = updateShippingOrderPort;
    }

    @Override
    @Transactional
    public ShippingOrder dockShip(final DockShipCommand command) {
        final ShippingOrderId id = command.shippingOrderId();
        LOGGER.info("Docking the Ship carrying Shipping Order with ID {}", id.id());
        final ShippingOrder shippingOrder = loadShippingOrderPort.loadById(id).orElseThrow(
            () -> ShippingOrderNotFoundException.forId(id)
        );
        shippingOrder.dockShip();
        updateShippingOrderPort.updateShippingOrder(shippingOrder);
        LOGGER.info("Ship with Vessel No: {} has docked at the port", shippingOrder.getVesselNumber());
        return shippingOrder;
    }
}
