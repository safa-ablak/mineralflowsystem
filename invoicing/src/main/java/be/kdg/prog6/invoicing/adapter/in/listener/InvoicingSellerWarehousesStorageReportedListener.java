package be.kdg.prog6.invoicing.adapter.in.listener;

import be.kdg.prog6.common.event.warehousing.SellerWarehousesStorageReportedEvent;
import be.kdg.prog6.common.event.warehousing.WarehouseStorageReport;
import be.kdg.prog6.invoicing.domain.CustomerId;
import be.kdg.prog6.invoicing.port.in.command.CalculateStorageFeeCommand;
import be.kdg.prog6.invoicing.port.in.usecase.CalculateStorageFeeUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static be.kdg.prog6.common.BoundedContext.INVOICING;
import static be.kdg.prog6.invoicing.adapter.config.InvoicingMessagingTopology.INVOICING_SELLER_WAREHOUSES_STORAGE_REPORTED_QUEUE;

@Component
public class InvoicingSellerWarehousesStorageReportedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoicingSellerWarehousesStorageReportedListener.class);

    private final CalculateStorageFeeUseCase calculateStorageFeeUseCase;

    public InvoicingSellerWarehousesStorageReportedListener(final CalculateStorageFeeUseCase calculateStorageFeeUseCase) {
        this.calculateStorageFeeUseCase = calculateStorageFeeUseCase;
    }

    /// Listen to the `SellerWarehousesStorageReportedEvent` and calculate the storage fee for the Customer
    @RabbitListener(queues = INVOICING_SELLER_WAREHOUSES_STORAGE_REPORTED_QUEUE)
    public void onSellerWarehousesStorageReported(final SellerWarehousesStorageReportedEvent event) {
        final CustomerId customerId = CustomerId.of(event.sellerId()); // Context Mapping: Seller is our Customer
        final List<WarehouseStorageReport> warehouseStorageReports = event.warehouseStorageReports();
        LOGGER.info(
            "Received {} at {} for Customer {} ({} Warehouse(s) reported)",
            event.getClass().getSimpleName(),
            INVOICING,
            customerId,
            warehouseStorageReports.size()
        );
        final CalculateStorageFeeCommand command = new CalculateStorageFeeCommand(
            customerId,
            warehouseStorageReports
        );
        calculateStorageFeeUseCase.calculateStorageFee(command);
    }
}