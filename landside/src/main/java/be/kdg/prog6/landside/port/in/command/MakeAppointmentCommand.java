package be.kdg.prog6.landside.port.in.command;

import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.SupplierId;
import be.kdg.prog6.landside.domain.TruckLicensePlate;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

public record MakeAppointmentCommand(
    SupplierId supplierId,
    TruckLicensePlate truckLicensePlate,
    RawMaterial rawMaterial,
    LocalDateTime scheduledArrivalTime
) {
    public MakeAppointmentCommand {
        requireNonNull(supplierId);
        requireNonNull(truckLicensePlate);
        requireNonNull(rawMaterial);
        requireNonNull(scheduledArrivalTime);
    }
}
