package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.port.in.usecase.scheduled.ReportStorageForWarehousesTask;
import be.kdg.prog6.warehousing.port.out.LoadSellerPort;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import be.kdg.prog6.warehousing.port.out.SellerWarehousesStorageReportedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class ReportStorageForWarehousesTaskImpl implements ReportStorageForWarehousesTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportStorageForWarehousesTaskImpl.class);

    private final LoadSellerPort loadSellerPort;
    private final LoadWarehousePort loadWarehousePort;
    private final SellerWarehousesStorageReportedPort sellerWarehousesStorageReportedPort;
    private final Clock clock;

    public ReportStorageForWarehousesTaskImpl(final LoadSellerPort loadSellerPort,
                                              final LoadWarehousePort loadWarehousePort,
                                              final SellerWarehousesStorageReportedPort sellerWarehousesStorageReportedPort,
                                              final Clock clock) {
        this.loadSellerPort = loadSellerPort;
        this.loadWarehousePort = loadWarehousePort;
        this.sellerWarehousesStorageReportedPort = sellerWarehousesStorageReportedPort;
        this.clock = clock;
    }

    @Override
//    @Scheduled(cron = "0 0 9 * * *", zone = "Europe/Brussels") // At 9:00 AM every day.
    @Scheduled(cron = "0 * * * * *", zone = "Europe/Brussels") // For testing purposes, every minute.
    public void reportStorageForWarehouses() {
        final LocalDateTime reportingDateTime = LocalDateTime.now(clock);
        LOGGER.info("Reporting storage for Warehouses of all Sellers at {}", KDG);
        final List<SellerId> sellerIds = loadSellerPort.loadAllSellerIds();
        for (SellerId sellerId : sellerIds) {
            final List<Warehouse> sellerWarehouses = loadWarehousePort.loadWarehousesBySellerIdForReporting(sellerId);
            LOGGER.info("Reporting storage for {} Warehouses of Seller {}", sellerWarehouses.size(), sellerId.id());
            // Report storage for each warehouse using the given reporting date
            sellerWarehousesStorageReportedPort.storageReported(sellerId, sellerWarehouses, reportingDateTime);
        }
        LOGGER.info("Warehouse storage reporting completed for all Sellers");
    }
}