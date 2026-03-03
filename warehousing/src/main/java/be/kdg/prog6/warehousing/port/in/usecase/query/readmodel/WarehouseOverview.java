package be.kdg.prog6.warehousing.port.in.usecase.query.readmodel;

import be.kdg.prog6.warehousing.domain.Seller;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.storage.*;

import java.math.BigDecimal;

public record WarehouseOverview(
    WarehouseId warehouseId,
    WarehouseNumber warehouseNumber,
    RawMaterial rawMaterial,
    BigDecimal balance,
    BigDecimal percentageFilled,
    SellerId sellerId,
    String sellerName,
    SiteLocation siteLocation
) {
    public static WarehouseOverview from(final Warehouse warehouse, final Seller seller) {
        final StockLevel stockLevel = warehouse.calculateStockLevel();
        return new WarehouseOverview(
            warehouse.getWarehouseId(),
            warehouse.getWarehouseNumber(),
            warehouse.getRawMaterial(),
            stockLevel.balance().amount(),
            stockLevel.percentageFilled(),
            seller.getSellerId(),
            seller.getName(),
            warehouse.getSiteLocation()
        );
    }
}
