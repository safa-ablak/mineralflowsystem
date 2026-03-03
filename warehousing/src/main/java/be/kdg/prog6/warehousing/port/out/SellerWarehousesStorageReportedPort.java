package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;

import java.time.LocalDateTime;
import java.util.List;

@FunctionalInterface
public interface SellerWarehousesStorageReportedPort {
    void storageReported(
        SellerId sellerId,
        List<Warehouse> sellerWarehouses,
        LocalDateTime reportingDateTime
    );
}
