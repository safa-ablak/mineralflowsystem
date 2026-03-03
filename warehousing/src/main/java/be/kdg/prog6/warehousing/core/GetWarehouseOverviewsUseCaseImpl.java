package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.Seller;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetWarehouseOverviewsUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.WarehouseOverview;
import be.kdg.prog6.warehousing.port.out.LoadSellerPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static be.kdg.prog6.common.ProjectInfo.KDG;


@Service
public class GetWarehouseOverviewsUseCaseImpl implements GetWarehouseOverviewsUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetWarehouseOverviewsUseCaseImpl.class);

    private final LoadWarehousePort loadWarehousePort;
    private final LoadSellerPort loadSellerPort;

    public GetWarehouseOverviewsUseCaseImpl(final LoadWarehousePort loadWarehousePort,
                                            final LoadSellerPort loadSellerPort) {
        this.loadWarehousePort = loadWarehousePort;
        this.loadSellerPort = loadSellerPort;
    }

    @Override
    public List<WarehouseOverview> getWarehouseOverviews() {
        LOGGER.info("Returning overview of all Warehouses at {}", KDG);
        return loadWarehousePort.loadWarehouses()
            .stream()
            .map(warehouse -> {
                final SellerId sellerId = warehouse.getSellerId();
                final Seller seller = loadSellerPort.loadSellerById(sellerId).orElseThrow();
                return WarehouseOverview.from(warehouse, seller);
            })
            .toList();
    }
}
