package be.kdg.prog6.landside.port.out;

import be.kdg.prog6.landside.domain.AppointmentId;
import be.kdg.prog6.landside.domain.TruckLicensePlate;
import be.kdg.prog6.landside.domain.Visit;

import java.util.List;
import java.util.Optional;

public interface LoadVisitPort {
    /**
     * Retrieves the active visit associated with a truck's license plate.
     * The visit is considered active if it has arrived but does not yet have a departure time.
     *
     * @param truckLicensePlate The truck's license plate.
     * @return An Optional containing the active Visit if found, or empty if not found.
     */
    Optional<Visit> loadActiveVisitByTruckLicensePlate(TruckLicensePlate truckLicensePlate);

    /**
     * Loads all active visits, i.e., visits where departure time is null and arrival time is not null.
     *
     * @return a list of active visits.
     */
    List<Visit> loadAllActiveVisits();

    int countActiveVisits();

    /**
     * Loads a visit by its appointment ID.
     *
     * @param appointmentId The appointment ID associated with the visit.
     * @return An Optional containing the Visit if found, or empty if not found.
     */
    Optional<Visit> loadVisitByAppointmentId(AppointmentId appointmentId);
}
