package be.kdg.prog6.landside.adapter.in.web.dto;

import be.kdg.prog6.landside.port.in.usecase.model.DockedTruck;

public record DockedTruckDto(
    String truckLicensePlate,
    String dockNumber,
    String weighBridgeNumber
) {
    public static DockedTruckDto from(final DockedTruck dockedTruck) {
        return new DockedTruckDto(
            dockedTruck.truckLicensePlate().value(),
            dockedTruck.dockNumber(),
            dockedTruck.weighBridgeNumber()
        );
    }
}
