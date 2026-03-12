package be.kdg.prog6.common.event.warehousing;

import java.util.List;
import java.util.UUID;

public record SellerWarehousesStorageReportedEvent(
    UUID sellerId,
    List<WarehouseStorageReport> warehouseStorageReports
) {}