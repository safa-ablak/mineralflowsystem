package be.kdg.prog6.landside.adapter.in.web.dto;

import be.kdg.prog6.landside.domain.Visit;
import be.kdg.prog6.landside.domain.VisitStatus;

import java.time.LocalDateTime;

public record VisitDto(
    String visitId,
    String appointmentId,
    WeighBridgeTicketDto weighBridgeTicket,
    LocalDateTime arrivalTime,
    LocalDateTime departureTime,
    VisitStatus status,
    boolean onSite // on-site or not
) {
    public static VisitDto fromDomain(final Visit visit) {
        final boolean hasWeighBridgeTransaction = visit.hasWeighBridgeTransaction();
        return new VisitDto(
            visit.getVisitId().id().toString(),
            visit.getAppointmentId().id().toString(),
            hasWeighBridgeTransaction ? WeighBridgeTicketDto.fromDomain(visit.getWeighBridgeTransaction()) : null,
            visit.getArrivalTime(),
            visit.getDepartureTime(),
            visit.getStatus(),
            visit.isTruckOnSite()
        );
    }
}