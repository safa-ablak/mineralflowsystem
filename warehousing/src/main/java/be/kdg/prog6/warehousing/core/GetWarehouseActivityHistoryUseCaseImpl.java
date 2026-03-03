package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.Seller;
import be.kdg.prog6.warehousing.domain.exception.storage.WarehouseNotFoundException;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.query.GetWarehouseActivityHistoryQuery;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetWarehouseActivityHistoryUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.WarehouseActivityHistory;
import be.kdg.prog6.warehousing.port.out.LoadSellerPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetWarehouseActivityHistoryUseCaseImpl implements GetWarehouseActivityHistoryUseCase {
    private final Logger LOGGER = LoggerFactory.getLogger(GetWarehouseActivityHistoryUseCaseImpl.class);

    private final LoadWarehousePort loadWarehousePort;
    private final LoadSellerPort loadSellerPort;

    public GetWarehouseActivityHistoryUseCaseImpl(final LoadWarehousePort loadWarehousePort, final LoadSellerPort loadSellerPort) {
        this.loadWarehousePort = loadWarehousePort;
        this.loadSellerPort = loadSellerPort;
    }

    @Override
    public WarehouseActivityHistory getWarehouseActivityHistory(final GetWarehouseActivityHistoryQuery query) {
        final WarehouseId warehouseId = query.warehouseId();
        final GetWarehouseActivityHistoryQuery.ViewMode viewMode = query.viewMode();
        LOGGER.info("Getting Activity History of Warehouse with ID {} View Mode <{}>",
            warehouseId.id(), viewMode
        );
        final Warehouse warehouse = (switch (viewMode) {
            case WITHOUT_ALLOCATIONS -> loadWarehousePort.loadWarehouseByIdWithAllActivities(warehouseId);
            case WITH_ALLOCATIONS -> loadWarehousePort.loadWarehouseByIdWithAllActivitiesAndAllocations(warehouseId);
        }).orElseThrow(() -> WarehouseNotFoundException.forId(warehouseId));

        LoggingUtils.logWarehouseActivitiesIfDebug(warehouse, viewMode);

        final Seller seller = loadSellerPort.loadSellerById(warehouse.getSellerId()).orElseThrow();
        return WarehouseActivityHistory.from(warehouse, seller);
    }
}
