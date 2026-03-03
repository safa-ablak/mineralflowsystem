package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.domain.storage.ShipmentAllocation;

import java.math.BigDecimal;

public record ShipmentAllocationDto(String deliveryId, String shipmentId, BigDecimal amountAllocated) {
    public static ShipmentAllocationDto fromDomain(final ShipmentAllocation allocation) {
        return new ShipmentAllocationDto(
            allocation.deliveryId().id().toString(),
            allocation.shipmentId().id().toString(),
            allocation.amountAllocated()
        );
    }
}