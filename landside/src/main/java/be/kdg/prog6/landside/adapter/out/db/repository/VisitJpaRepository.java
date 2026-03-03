package be.kdg.prog6.landside.adapter.out.db.repository;

import be.kdg.prog6.landside.adapter.out.db.entity.VisitJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitJpaRepository extends JpaRepository<VisitJpaEntity, UUID> {
    /**
     * Finds the most recent active {@link VisitJpaEntity} by the associated truck license plate.
     * <p>
     * An active visit is defined as one where the arrival time is not null and the departure time is null.
     * The result is ordered by arrival time descending to ensure the latest visit is returned first.
     *
     * @param truckLicensePlate the license plate of the truck
     * @return an {@link Optional} containing the most recent active visit, if one exists
     */
    @Query("SELECT v FROM VisitJpaEntity v WHERE v.truckLicensePlate = :truckLicensePlate AND v.arrivalTime IS NOT NULL AND v.departureTime IS NULL ORDER BY v.arrivalTime DESC")
    // ORDER BY just to be safe - in case of overlapping entries(unrealistic scenario should not happen)
    Optional<VisitJpaEntity> findActiveVisitByLicensePlate(@Param("truckLicensePlate") String truckLicensePlate);

    /**
     * Retrieves all visits that are considered "active," i.e., those where arrival time is not null and departure time is null.
     *
     * @return a list of active visits.
     */
    @Query("SELECT v FROM VisitJpaEntity v WHERE v.arrivalTime IS NOT NULL AND v.departureTime IS NULL ")
    List<VisitJpaEntity> findAllActiveVisits();

    /**
     * Counts the number of active visits, defined as visits where arrival time is not null and departure time is null.
     *
     * @return the count of active visits.
     */
    @Query("SELECT COUNT(v) FROM VisitJpaEntity v WHERE v.arrivalTime IS NOT NULL AND v.departureTime IS NULL")
    int countActiveVisits();

    Optional<VisitJpaEntity> findByAppointmentId(UUID appointmentId);
}
