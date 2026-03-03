package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.in.usecase.query.GetShippingOrdersUseCase;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetShippingOrdersUseCaseImpl implements GetShippingOrdersUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetShippingOrdersUseCaseImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;

    public GetShippingOrdersUseCaseImpl(final LoadShippingOrderPort loadShippingOrderPort) {
        this.loadShippingOrderPort = loadShippingOrderPort;
    }

    @Override
    public List<ShippingOrder> getShippingOrders() {
        LOGGER.info("Getting all Shipping Orders at {}.", KDG);
        final List<ShippingOrder> shippingOrders = loadShippingOrderPort.loadShippingOrders();
        LOGGER.info("Found {} Shipping Orders", shippingOrders.size());
        return shippingOrders;
    }
}
