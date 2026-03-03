package be.kdg.prog6.landside.adapter.in.web.dto;

import be.kdg.prog6.landside.domain.Appointment;

import java.time.LocalDateTime;

public record AppointmentDto(
    String appointmentId,
    String supplierId,
    String warehouseId,
    String truckLicensePlate,
    String rawMaterial,
    LocalDateTime arrivalWindowStart,
    LocalDateTime arrivalWindowEnd,
    String status
) {
    public static AppointmentDto fromDomain(final Appointment appointment) {
        return new AppointmentDto(
            appointment.getAppointmentId().id().toString(),
            appointment.getSupplierId().id().toString(),
            appointment.getWarehouseId().id().toString(),
            appointment.getTruckLicensePlate().value(),
            appointment.getRawMaterial().name(),
            appointment.getArrivalWindowStart(),
            appointment.getArrivalWindowEnd(),
            appointment.getStatus().name()
        );
    }
}
