package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.Seller;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.exception.storage.WarehouseNotFoundException;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetWarehouseOverviewUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.WarehouseOverview;
import be.kdg.prog6.warehousing.port.out.LoadSellerPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetWarehouseOverviewUseCaseImpl implements GetWarehouseOverviewUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetWarehouseOverviewUseCaseImpl.class);

    private final LoadWarehousePort loadWarehousePort;
    private final LoadSellerPort loadSellerPort;

    public GetWarehouseOverviewUseCaseImpl(final LoadWarehousePort loadWarehousePort,
                                           final LoadSellerPort loadSellerPort) {
        this.loadWarehousePort = loadWarehousePort;
        this.loadSellerPort = loadSellerPort;
    }

    @Override
    public WarehouseOverview getWarehouseOverview(final WarehouseId warehouseId) {
        LOGGER.info("Returning overview of Warehouse with ID {}", warehouseId.id());
        final Warehouse warehouse = loadWarehousePort.loadWarehouseById(warehouseId).orElseThrow(
            () -> WarehouseNotFoundException.forId(warehouseId)
        );
        final SellerId sellerId = warehouse.getSellerId();
        final Seller seller = loadSellerPort.loadSellerById(sellerId).orElseThrow();
        return WarehouseOverview.from(warehouse, seller);
    }
}
