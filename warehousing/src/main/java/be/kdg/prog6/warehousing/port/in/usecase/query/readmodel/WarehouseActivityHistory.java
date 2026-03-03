package be.kdg.prog6.warehousing.port.in.usecase.query.readmodel;

import be.kdg.prog6.warehousing.domain.Seller;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.storage.*;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public record WarehouseActivityHistory(
    WarehouseId warehouseId,
    WarehouseNumber warehouseNumber,
    RawMaterial rawMaterial,
    Balance balance,
    BigDecimal percentageFilled,
    SellerId sellerId,
    String sellerName,
    SiteLocation siteLocation,
    List<Delivery> deliveries,
    List<Shipment> shipments,
    List<ShipmentAllocation> allocations // Empty if not loaded
) {
    public static WarehouseActivityHistory from(final Warehouse warehouse, final Seller seller) {
        final StockLevel stockLevel = warehouse.calculateStockLevel();
        final List<Delivery> sortedDeliveries = sortDeliveriesByNewest(warehouse.getDeliveries());
        final List<Shipment> sortedShipments = sortShipmentsByNewest(warehouse.getShipments());
        return new WarehouseActivityHistory(
            warehouse.getWarehouseId(),
            warehouse.getWarehouseNumber(),
            warehouse.getRawMaterial(),
            stockLevel.balance(),
            stockLevel.percentageFilled(),
            seller.getSellerId(),
            seller.getName(),
            warehouse.getSiteLocation(),
            sortedDeliveries,
            sortedShipments,
            warehouse.getShipmentAllocations()
        );
    }

    private static List<Delivery> sortDeliveriesByNewest(final List<Delivery> deliveries) {
        return deliveries.stream()
            .sorted(Comparator.comparing(Delivery::time).reversed())
            .toList();
    }

    private static List<Shipment> sortShipmentsByNewest(final List<Shipment> shipments) {
        return shipments.stream()
            .sorted(Comparator.comparing(Shipment::time).reversed())
            .toList();
    }
}
