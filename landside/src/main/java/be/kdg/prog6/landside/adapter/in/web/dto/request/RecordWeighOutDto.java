package be.kdg.prog6.landside.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RecordWeighOutDto(
    @NotBlank(message = "Truck license plate is required")
    String truckLicensePlate,
    @Positive(message = "Tare weight must be greater than 0")
    BigDecimal tareWeight
) {}