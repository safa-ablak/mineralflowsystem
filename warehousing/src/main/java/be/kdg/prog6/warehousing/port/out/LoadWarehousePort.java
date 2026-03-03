package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoadWarehousePort {
    Optional<Warehouse> loadWarehouseById(WarehouseId id);

    List<Warehouse> loadWarehousesByRawMaterial(RawMaterial rawMaterial);

    List<Warehouse> loadWarehouses();

    List<Warehouse> loadWarehousesBySellerId(SellerId sellerId);

    List<Warehouse> loadWarehousesBySellerIdForShipment(SellerId sellerId); // Loads all deliveries and allocations of the ledger

    List<Warehouse> loadWarehousesBySellerIdForReporting(SellerId sellerId); // Loads all deliveries and allocations of the ledger

    // To query Warehouse Balance change between two given dates, [from <= activity time <= to]
    Optional<Warehouse> loadWarehouseByIdWithActivitiesBetween(WarehouseId id, LocalDateTime from, LocalDateTime to);

    Optional<Warehouse> loadWarehouseByIdWithAllActivities(WarehouseId id); // Loads all deliveries and shipments of the ledger

    Optional<Warehouse> loadWarehouseByIdWithAllActivitiesAndAllocations(WarehouseId id); // Full-fledged with shipment allocations included
}
