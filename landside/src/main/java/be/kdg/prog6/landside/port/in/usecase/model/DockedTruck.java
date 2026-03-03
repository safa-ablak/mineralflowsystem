package be.kdg.prog6.landside.port.in.usecase.model;

import be.kdg.prog6.landside.domain.TruckLicensePlate;

/**
 * This is a use case response model returned after executing a command.
 * Not a read model – it is not persisted or used for querying.
 */
public record DockedTruck(
    TruckLicensePlate truckLicensePlate,
    String dockNumber,
    String weighBridgeNumber
) {
}
