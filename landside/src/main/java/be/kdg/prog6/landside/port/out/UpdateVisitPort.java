package be.kdg.prog6.landside.port.out;

import be.kdg.prog6.landside.domain.Visit;

public interface UpdateVisitPort {
    /**
     * Saves or updates a visit. If the visit already exists, it will be updated;
     * otherwise, it will be inserted as a new visit.
     *
     * @param visit the visit to be saved or updated
     */
    void updateVisit(Visit visit);
}
