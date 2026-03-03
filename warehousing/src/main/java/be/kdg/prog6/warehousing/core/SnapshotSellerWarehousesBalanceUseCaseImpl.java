package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.storage.Balance;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.port.in.command.SnapshotSellerWarehousesBalanceCommand;
import be.kdg.prog6.warehousing.port.in.usecase.SnapshotSellerWarehousesBalanceUseCase;
import be.kdg.prog6.warehousing.port.out.BalanceSnapshottedPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SnapshotSellerWarehousesBalanceUseCaseImpl implements SnapshotSellerWarehousesBalanceUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnapshotSellerWarehousesBalanceUseCaseImpl.class);

    private final LoadWarehousePort loadWarehousePort;
    private final BalanceSnapshottedPort balanceSnapshottedPort;
    private final Clock clock;

    public SnapshotSellerWarehousesBalanceUseCaseImpl(final LoadWarehousePort loadWarehousePort,
                                                      final BalanceSnapshottedPort balanceSnapshottedPort,
                                                      final Clock clock) {
        this.loadWarehousePort = loadWarehousePort;
        this.balanceSnapshottedPort = balanceSnapshottedPort;
        this.clock = clock;
    }

    @Override
    @Transactional
    public void snapshot(final SnapshotSellerWarehousesBalanceCommand command) {
        final SellerId sellerId = command.sellerId();
        final List<Warehouse> sellerWarehouses = loadWarehousePort.loadWarehousesBySellerId(sellerId);

        LOGGER.info("\n>>>>> Taking Balance snapshots for the Warehouses of Seller {}", sellerId.id());
        for (Warehouse warehouse : sellerWarehouses) {
            warehouse.snapshotBalance(LocalDateTime.now(clock)); // Take a snapshot of the warehouse's balance.
            final Balance baseBalance = warehouse.getBaseBalance();
            LOGGER.debug(
                "\n>>>>> Snapshot taken for Warehouse {}: Base Balance updated to {} tons at {}",
                warehouse.getWarehouseId().id(),
                String.format("%.2f", baseBalance.amount()),
                baseBalance.time()
            );
            balanceSnapshottedPort.balanceSnapshotted(warehouse);
        }
        LOGGER.info("Balance snapshots taken for {} Warehouses of Seller {}",
            sellerWarehouses.size(), sellerId.id()
        );
    }
}
