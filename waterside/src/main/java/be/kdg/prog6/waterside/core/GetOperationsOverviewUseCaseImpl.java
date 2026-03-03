package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.ShippingOrderId;
import be.kdg.prog6.waterside.domain.exception.shippingorder.ShippingOrderNotFoundException;
import be.kdg.prog6.waterside.port.in.usecase.query.GetOperationsOverviewUseCase;
import be.kdg.prog6.waterside.port.in.usecase.query.readmodel.OperationsOverview;
import be.kdg.prog6.waterside.port.out.LoadShippingOrderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetOperationsOverviewUseCaseImpl implements GetOperationsOverviewUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetOperationsOverviewUseCaseImpl.class);

    private final LoadShippingOrderPort loadShippingOrderPort;

    public GetOperationsOverviewUseCaseImpl(final LoadShippingOrderPort loadShippingOrderPort) {
        this.loadShippingOrderPort = loadShippingOrderPort;
    }

    @Override
    public OperationsOverview getOperationsOverview(final ShippingOrderId id) {
        LOGGER.info("Getting Operations Overview for Shipping Order with ID {}", id.id());
        final ShippingOrder shippingOrder = loadShippingOrderPort.loadById(id).orElseThrow(
            () -> ShippingOrderNotFoundException.forId(id)
        );
        // Map to read model and return
        final OperationsOverview overview = OperationsOverview.fromDomain(shippingOrder);
        LOGGER.info("Retrieved Operations Overview for Shipping Order with ID {}", id.id());
        return overview;
    }
}
