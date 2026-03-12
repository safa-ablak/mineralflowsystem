package be.kdg.prog6.landside.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RecordWeighInDto(
    @NotBlank(message = "Truck license plate is required")
    String truckLicensePlate,
    @Positive(message = "Gross weight must be greater than 0")
    BigDecimal grossWeight
) {}