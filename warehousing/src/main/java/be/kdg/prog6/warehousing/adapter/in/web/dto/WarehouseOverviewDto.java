package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.WarehouseOverview;

import java.math.BigDecimal;

public record WarehouseOverviewDto(
    String warehouseId,
    String warehouseNumber,
    // SellerDto seller,
    String sellerId,
    String sellerName,
    String rawMaterial,
    BigDecimal balance,
    BigDecimal percentageFilled,
    SiteLocationDto siteLocation
) {
    public static WarehouseOverviewDto from(final WarehouseOverview overview) {
        return new WarehouseOverviewDto(
            overview.warehouseId().id().toString(),
            overview.warehouseNumber().value(),
            overview.sellerId().id().toString(),
            overview.sellerName(),
            overview.rawMaterial().name(),
            overview.balance(),
            overview.percentageFilled(),
            SiteLocationDto.of(overview.siteLocation())
        );
    }
}