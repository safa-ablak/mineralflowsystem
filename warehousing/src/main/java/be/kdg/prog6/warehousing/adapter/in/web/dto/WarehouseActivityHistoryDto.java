package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.WarehouseActivityHistory;

import java.math.BigDecimal;
import java.util.List;

public record WarehouseActivityHistoryDto(
    String warehouseId,
    String warehouseNumber,
    String rawMaterial,
    BalanceDto balance,
    BigDecimal percentageFilled,
    String sellerId,
    String sellerName,
    SiteLocationDto siteLocation,
    List<DeliveryDto> deliveries,
    List<ShipmentDto> shipments,
    List<ShipmentAllocationDto> allocations
) {
    public static WarehouseActivityHistoryDto from(final WarehouseActivityHistory warehouseActivityHistory) {
        return new WarehouseActivityHistoryDto(
            warehouseActivityHistory.warehouseId().id().toString(),
            warehouseActivityHistory.warehouseNumber().value(),
            warehouseActivityHistory.rawMaterial().name(),
            BalanceDto.of(warehouseActivityHistory.balance()),
            warehouseActivityHistory.percentageFilled(),
            warehouseActivityHistory.sellerId().id().toString(),
            warehouseActivityHistory.sellerName(),
            SiteLocationDto.of(warehouseActivityHistory.siteLocation()),
            warehouseActivityHistory.deliveries().stream().map(DeliveryDto::fromDomain).toList(),
            warehouseActivityHistory.shipments().stream().map(ShipmentDto::fromDomain).toList(),
            warehouseActivityHistory.allocations().stream().map(ShipmentAllocationDto::fromDomain).toList()
        );
    }
}
