package be.kdg.prog6.landside.adapter.in.web.dto;

import be.kdg.prog6.landside.domain.WeighBridgeTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WeighBridgeTicketDto(
    BigDecimal grossWeight,
    BigDecimal tareWeight,
    BigDecimal netWeight,
    LocalDateTime weighInTime,
    LocalDateTime weighOutTime,
    String truckLicensePlate
) {
    public static WeighBridgeTicketDto fromDomain(final WeighBridgeTransaction transaction) {
        BigDecimal netWeight = null;
        if (transaction.isWeighOutRecorded()) {
            netWeight = transaction.calculateNetWeight();
        }
        return new WeighBridgeTicketDto(
            transaction.getGrossWeight(),
            transaction.getTareWeight(),
            netWeight,
            transaction.getWeighInTime(),
            transaction.getWeighOutTime(),
            transaction.getTruckLicensePlate().value()
        );
    }
}
