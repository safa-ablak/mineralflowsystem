package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.in.usecase.query.GetShippingOrdersOnSiteUseCase;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetShippingOrdersOnSiteUseCaseImpl implements GetShippingOrdersOnSiteUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetShippingOrdersOnSiteUseCaseImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;

    public GetShippingOrdersOnSiteUseCaseImpl(final LoadShippingOrderPort loadShippingOrderPort) {
        this.loadShippingOrderPort = loadShippingOrderPort;
    }

    @Override
    public List<ShippingOrder> getShippingOrdersOnSite() {
        LOGGER.info("Getting all Shipping Orders on {}'s site", KDG);
        final List<ShippingOrder> shippingOrders = loadShippingOrderPort.loadShippingOrdersOnSite();
        LOGGER.info("Found {} Shipping Orders On Site", shippingOrders.size());
        return shippingOrders;
    }
}
