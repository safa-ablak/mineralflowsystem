package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.exception.storage.WarehouseNotFoundException;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.query.CalculateWarehouseBalanceChangeQuery;
import be.kdg.prog6.warehousing.port.in.usecase.query.CalculateWarehouseBalanceChangeUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.NetBalanceChange;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CalculateWarehouseBalanceChangeUseCaseImpl implements CalculateWarehouseBalanceChangeUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateWarehouseBalanceChangeUseCaseImpl.class);

    private final LoadWarehousePort loadWarehousePort;

    public CalculateWarehouseBalanceChangeUseCaseImpl(final LoadWarehousePort loadWarehousePort) {
        this.loadWarehousePort = loadWarehousePort;
    }

    /**
     * Calculates the net balance change of a warehouse between two given dates.
     * <p>
     * This is done by loading only the deliveries and shipments that occurred within the given period,
     * and projecting them on top of an initial balance of 0 (as of {@code LocalDateTime.MIN}).
     *
     * @param query the warehouse ID and time window
     * @return the net balance change (deliveries - shipments) within the specified time window
     */
    @Override
    public NetBalanceChange calculateNetBalanceChange(final CalculateWarehouseBalanceChangeQuery query) {
        final WarehouseId id = query.warehouseId();
        final LocalDateTime from = query.from();
        final LocalDateTime to = query.to();

        LOGGER.info("Calculating net Balance change of Warehouse with ID {} from {} to {}", id.id(), from, to);
        final Warehouse warehouse = loadWarehousePort
            .loadWarehouseByIdWithActivitiesBetween(id, from, to).orElseThrow(
                () -> WarehouseNotFoundException.forId(id)
            );
        LoggingUtils.logWarehouseActivitiesIfDebug(warehouse);

        // Computes the net Balance change (delta) between Deliveries and Shipments – it can be positive or negative
        final BigDecimal netBalanceChange = warehouse.balance();
        return new NetBalanceChange(id, from, to, netBalanceChange);
    }
}