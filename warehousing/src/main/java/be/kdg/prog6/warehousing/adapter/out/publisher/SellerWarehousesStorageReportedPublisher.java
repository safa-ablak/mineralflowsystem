package be.kdg.prog6.warehousing.adapter.out.publisher;

import be.kdg.prog6.common.event.warehousing.SellerWarehousesStorageReportedEvent;
import be.kdg.prog6.common.event.warehousing.StorageEntry;
import be.kdg.prog6.common.event.warehousing.WarehouseStorageReport;
import be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.port.out.SellerWarehousesStorageReportedPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SellerWarehousesStorageReportedPublisher implements SellerWarehousesStorageReportedPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(SellerWarehousesStorageReportedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public SellerWarehousesStorageReportedPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void storageReported(final SellerId sellerId, final List<Warehouse> sellerWarehouses, final LocalDateTime reportingDateTime) {
        final String routingKey = WarehousingMessagingTopology.SELLER_WAREHOUSES_STORAGE_REPORTED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        final List<WarehouseStorageReport> warehouseStorageReports = sellerWarehouses.stream()
            .map(warehouse -> toWarehouseStorageReport(warehouse, reportingDateTime))
            .toList();
        rabbitTemplate.convertAndSend(
            WarehousingMessagingTopology.WAREHOUSING_EVENTS_EXCHANGE,
            routingKey,
            new SellerWarehousesStorageReportedEvent(
                sellerId.id(),
                warehouseStorageReports
            )
        );
    }

    private static WarehouseStorageReport toWarehouseStorageReport(final Warehouse warehouse, final LocalDateTime reportingDateTime) {
        final List<StorageEntry> storageEntries = warehouse
            .getStoredDeliveryRemainders(reportingDateTime)
            .stream()
            .map(r -> new StorageEntry(r.remainingAmount(), r.daysStored()))
            .toList();
        return new WarehouseStorageReport(
            warehouse.getWarehouseId().id(),
            warehouse.getRawMaterial().name(),
            storageEntries
        );
    }
}
