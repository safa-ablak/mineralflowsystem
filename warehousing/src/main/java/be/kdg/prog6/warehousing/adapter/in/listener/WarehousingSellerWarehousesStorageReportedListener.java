package be.kdg.prog6.warehousing.adapter.in.listener;

import be.kdg.prog6.common.event.warehousing.SellerWarehousesStorageReportedEvent;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.port.in.command.SnapshotSellerWarehousesBalanceCommand;
import be.kdg.prog6.warehousing.port.in.usecase.SnapshotSellerWarehousesBalanceUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.WAREHOUSING;
import static be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology.WAREHOUSING_SELLER_WAREHOUSES_STORAGE_REPORTED_QUEUE;

@Component
public class WarehousingSellerWarehousesStorageReportedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehousingSellerWarehousesStorageReportedListener.class);

    private final SnapshotSellerWarehousesBalanceUseCase snapshotSellerWarehousesBalanceUseCase;

    public WarehousingSellerWarehousesStorageReportedListener(final SnapshotSellerWarehousesBalanceUseCase snapshotSellerWarehousesBalanceUseCase) {
        this.snapshotSellerWarehousesBalanceUseCase = snapshotSellerWarehousesBalanceUseCase;
    }

    /// Take a daily snapshot of the balance for each warehouse of the Seller after receiving the `SellerWarehousesStorageReportedEvent` event.
    @RabbitListener(queues = WAREHOUSING_SELLER_WAREHOUSES_STORAGE_REPORTED_QUEUE)
    public void onSellerWarehousesStorageReported(final SellerWarehousesStorageReportedEvent event) {
        LOGGER.info(
            "Received {} at {} for Seller {} ({} Warehouse(s) reported)",
            event.getClass().getSimpleName(),
            WAREHOUSING,
            event.sellerId(),
            event.warehouseStorageReports().size()
        );
        final SnapshotSellerWarehousesBalanceCommand command = new SnapshotSellerWarehousesBalanceCommand(
            SellerId.of(event.sellerId())
        );
        snapshotSellerWarehousesBalanceUseCase.snapshot(command);
    }
}
