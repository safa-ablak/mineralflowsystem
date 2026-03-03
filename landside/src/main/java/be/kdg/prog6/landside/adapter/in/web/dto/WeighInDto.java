package be.kdg.prog6.landside.adapter.in.web.dto;

import be.kdg.prog6.landside.domain.WeighBridgeTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WeighInDto(
    String truckLicensePlate,
    BigDecimal grossWeight,
    LocalDateTime weighInTime
) {
    public static WeighInDto fromDomain(final WeighBridgeTransaction transaction) {
        return new WeighInDto(
            transaction.getTruckLicensePlate().value(),
            transaction.getGrossWeight(),
            transaction.getWeighInTime()
        );
    }
}
