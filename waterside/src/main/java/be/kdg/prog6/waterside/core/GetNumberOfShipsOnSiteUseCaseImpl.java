package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.port.in.usecase.query.GetNumberOfShipsOnSiteUseCase;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetNumberOfShipsOnSiteUseCaseImpl implements GetNumberOfShipsOnSiteUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetNumberOfShipsOnSiteUseCaseImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;

    public GetNumberOfShipsOnSiteUseCaseImpl(final LoadShippingOrderPort loadShippingOrderPort) {
        this.loadShippingOrderPort = loadShippingOrderPort;
    }

    @Override
    public int getNumberOfShipsOnSite() {
        LOGGER.info("Getting the number of ships on {}'s site", KDG);
        final int numberOfShipsOnSite = loadShippingOrderPort.countShippingOrdersOnSite();
        LOGGER.info("There are {} ships on site", numberOfShipsOnSite);
        return numberOfShipsOnSite;
    }
}
