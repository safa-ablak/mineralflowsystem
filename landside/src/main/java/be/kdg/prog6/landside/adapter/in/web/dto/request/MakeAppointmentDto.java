package be.kdg.prog6.landside.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MakeAppointmentDto(
    @NotBlank(message = "Truck license plate is required")
    String truckLicensePlate,
    @NotBlank(message = "Raw material is required")
    String rawMaterial,
    @NotNull(message = "Scheduled arrival time is required")
    LocalDateTime scheduledArrivalTime
) {
}

