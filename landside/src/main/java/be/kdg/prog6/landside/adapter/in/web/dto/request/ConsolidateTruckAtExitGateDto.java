package be.kdg.prog6.landside.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ConsolidateTruckAtExitGateDto(
    @NotBlank(message = "Truck license plate is required")
    String truckLicensePlate
) {
}
