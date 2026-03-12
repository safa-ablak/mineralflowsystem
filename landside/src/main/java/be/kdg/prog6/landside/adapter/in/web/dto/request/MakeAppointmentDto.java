package be.kdg.prog6.landside.adapter.in.web.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record MakeAppointmentDto(
    @Nullable // If Admin is making an appointment on behalf of a Supplier; won't be provided by Suppliers as it is taken from JWT
    UUID supplierId,
    @NotBlank(message = "Truck license plate is required")
    String truckLicensePlate,
    @NotBlank(message = "Raw material is required")
    String rawMaterial,
    @NotNull(message = "Scheduled arrival time is required")
    LocalDateTime scheduledArrivalTime
) {}

