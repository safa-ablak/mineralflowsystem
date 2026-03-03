package be.kdg.prog6.waterside.adapter.in.web.dto;

import be.kdg.prog6.waterside.domain.ShippingOrder;

import java.time.LocalDateTime;

public record ShippingOrderDto(
    String shippingOrderId,
    String referenceId,
    String vesselNumber,
    LocalDateTime scheduledArrivalDate,
    LocalDateTime scheduledDepartureDate,
    LocalDateTime actualArrivalDate,
    LocalDateTime actualDepartureDate,
    InspectionOperationDto inspectionOperation,
    BunkeringOperationDto bunkeringOperation,
    String status
) {
    public static ShippingOrderDto fromDomain(final ShippingOrder shippingOrder) {
        return new ShippingOrderDto(
            shippingOrder.getShippingOrderId().id().toString(),
            shippingOrder.getReferenceId().id().toString(),
            shippingOrder.getVesselNumber().value(),
            shippingOrder.getScheduledArrivalDate(),
            shippingOrder.getScheduledDepartureDate(),
            shippingOrder.getActualArrivalDate(),
            shippingOrder.getActualDepartureDate(),
            InspectionOperationDto.fromDomain(shippingOrder.getInspection()),
            BunkeringOperationDto.fromDomain(shippingOrder.getBunkering()),
            shippingOrder.getStatus().name()
        );
    }
}