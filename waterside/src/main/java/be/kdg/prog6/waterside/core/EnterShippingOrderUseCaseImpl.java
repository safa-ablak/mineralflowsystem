package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.in.command.EnterShippingOrderCommand;
import be.kdg.prog6.waterside.port.in.usecase.EnterShippingOrderUseCase;
import be.kdg.prog6.waterside.port.out.CreateShippingOrderPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnterShippingOrderUseCaseImpl implements EnterShippingOrderUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterShippingOrderUseCaseImpl.class);

    private final List<CreateShippingOrderPort> createShippingOrderPorts;

    public EnterShippingOrderUseCaseImpl(final List<CreateShippingOrderPort> createShippingOrderPorts) {
        this.createShippingOrderPorts = createShippingOrderPorts;
    }

    @Override
    @Transactional
    public ShippingOrder enterShippingOrder(final EnterShippingOrderCommand command) {
        LOGGER.info("Entering a Shipping Order");
        final ShippingOrder shippingOrder = new ShippingOrder(
            command.buyerId(),
            command.referenceId(),
            command.vesselNumber(),
            command.scheduledArrivalDate(),
            command.scheduledDepartureDate()
        );
        createShippingOrderPorts.forEach(
            port -> port.createShippingOrder(shippingOrder)
        );
        LOGGER.info("Shipping Order with ID {} has been entered", shippingOrder.getShippingOrderId().id());
        return shippingOrder;
    }
}
