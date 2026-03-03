package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.ShippingOrderId;
import be.kdg.prog6.waterside.domain.exception.shippingorder.ShippingOrderNotFoundException;
import be.kdg.prog6.waterside.port.in.usecase.query.GetShippingOrderUseCase;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetShippingOrderUseCaseImpl implements GetShippingOrderUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetShippingOrderUseCaseImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;

    public GetShippingOrderUseCaseImpl(final LoadShippingOrderPort loadShippingOrderPort) {
        this.loadShippingOrderPort = loadShippingOrderPort;
    }

    @Override
    public ShippingOrder getShippingOrder(final ShippingOrderId id) {
        LOGGER.info("Getting Shipping Order with ID {}", id.id());
        return loadShippingOrderPort.loadById(id).orElseThrow(
            () -> ShippingOrderNotFoundException.forId(id)
        );
    }
}
