package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ReferenceId;
import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.exception.shippingorder.ShippingOrderNotFoundException;
import be.kdg.prog6.waterside.port.in.command.DepartShipCommand;
import be.kdg.prog6.waterside.port.in.usecase.DepartShipUseCase;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import be.kdg.prog6.waterside.port.out.ShipDepartedPort;
import be.kdg.prog6.waterside.port.out.UpdateShippingOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DepartShipUseCaseImpl implements DepartShipUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartShipUseCaseImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;
    private final UpdateShippingOrderPort updateShippingOrderPort;
    private final ShipDepartedPort shipDepartedPort;

    public DepartShipUseCaseImpl(final LoadShippingOrderPort loadShippingOrderPort,
                                 final UpdateShippingOrderPort updateShippingOrderPort,
                                 final ShipDepartedPort shipDepartedPort) {
        this.loadShippingOrderPort = loadShippingOrderPort;
        this.updateShippingOrderPort = updateShippingOrderPort;
        this.shipDepartedPort = shipDepartedPort;
    }

    // Update the shipping order in the db (set the actual departure date) + publish the event.
    @Override
    @Transactional
    public void departShip(final DepartShipCommand command) {
        final ReferenceId referenceId = command.referenceId();
        LOGGER.info("Departing the Ship carrying Shipping Order via Reference ID {}", referenceId.id());

        // Load the shipping order from the db via the Reference ID
        final ShippingOrder shippingOrder = loadShippingOrderPort.loadByReferenceId(referenceId).orElseThrow(
            () -> ShippingOrderNotFoundException.forReferenceId(referenceId)
        );
        // Depart the ship
        shippingOrder.departShip();

        // Persist the updated shipping order + publish the event
        updateShippingOrderPort.updateShippingOrder(shippingOrder);
        shipDepartedPort.shipDeparted(shippingOrder);

        LOGGER.info("Ship with Vessel No: {} carrying Shipping Order with ID {} has now departed",
            shippingOrder.getVesselNumber(), shippingOrder.getShippingOrderId().id()
        );
    }
}
