package be.kdg.prog6.landside.domain.event;

import be.kdg.prog6.landside.domain.AppointmentId;
import be.kdg.prog6.landside.domain.TruckLicensePlate;

public record TruckDepartedEvent(
    TruckLicensePlate truckLicensePlate,
    AppointmentId appointmentId
) {}
