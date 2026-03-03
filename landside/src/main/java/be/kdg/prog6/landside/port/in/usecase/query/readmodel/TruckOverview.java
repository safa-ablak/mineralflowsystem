package be.kdg.prog6.landside.port.in.usecase.query.readmodel;

import be.kdg.prog6.landside.domain.Appointment;
import be.kdg.prog6.landside.domain.Visit;
import be.kdg.prog6.landside.domain.WeighBridgeTransaction;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Read model combining Appointment and (optional) Visit info for a truck.
 * Designed for query-side usage only.
 */
public record TruckOverview(
    String appointmentId,
    String supplierId,
    String visitId,
    String truckLicensePlate,
    String rawMaterial,
    LocalDateTime arrivalWindowStart,
    LocalDateTime arrivalWindowEnd,
    LocalDateTime actualArrivalDate,
    LocalDateTime dockTime,
    WeighBridgeTransaction weighBridgeTransaction,
    LocalDateTime departureTime,
    String appointmentStatus,
    String visitStatus
) {
    public static TruckOverview from(final Appointment appointment, final Optional<Visit> optionalVisit) {
        final Visit visit = optionalVisit.orElse(null);
        return new TruckOverview(
            appointment.getAppointmentId().id().toString(),
            appointment.getSupplierId().id().toString(),
            visit != null ? visit.getVisitId().id().toString() : null,
            appointment.getTruckLicensePlate().value(),
            appointment.getRawMaterial().name(),
            appointment.getArrivalWindowStart(),
            appointment.getArrivalWindowEnd(),
            visit != null ? visit.getArrivalTime() : null,
            visit != null ? visit.getDockTime() : null,
            visit != null && visit.hasWeighBridgeTransaction() ? visit.getWeighBridgeTransaction() : null,
            visit != null ? visit.getDepartureTime() : null,
            appointment.getStatus().name(),
            visit != null ? visit.getStatus().name() : null
        );
    }
}
