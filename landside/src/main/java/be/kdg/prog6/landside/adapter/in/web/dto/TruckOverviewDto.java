package be.kdg.prog6.landside.adapter.in.web.dto;

import be.kdg.prog6.landside.domain.WeighBridgeTransaction;
import be.kdg.prog6.landside.port.in.usecase.query.readmodel.TruckOverview;

import java.time.LocalDateTime;

public record TruckOverviewDto(
    String appointmentId,
    String supplierId,
    String visitId,
    String truckLicensePlate,
    String rawMaterial,
    LocalDateTime arrivalWindowStart,
    LocalDateTime arrivalWindowEnd,
    LocalDateTime actualArrivalDate,
    LocalDateTime dockTime,
    WeighBridgeTicketDto weighBridgeTicket,
    LocalDateTime departureTime,
    String appointmentStatus,
    String visitStatus
) {
    public static TruckOverviewDto from(final TruckOverview truckOverview) {
        WeighBridgeTicketDto weighBridgeTicket = null;
        final WeighBridgeTransaction transaction = truckOverview.weighBridgeTransaction();
        if (transaction != null) {
            weighBridgeTicket = WeighBridgeTicketDto.fromDomain(transaction);
        }
        return new TruckOverviewDto(
            truckOverview.appointmentId(),
            truckOverview.supplierId(),
            truckOverview.visitId(),
            truckOverview.truckLicensePlate(),
            truckOverview.rawMaterial(),
            truckOverview.arrivalWindowStart(),
            truckOverview.arrivalWindowEnd(),
            truckOverview.actualArrivalDate(),
            truckOverview.dockTime(),
            weighBridgeTicket,
            truckOverview.departureTime(),
            truckOverview.appointmentStatus(),
            truckOverview.visitStatus()
        );
    }
}
