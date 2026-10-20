package be.kdg.prog6.landside.domain.exception;

import static be.kdg.prog6.common.ProjectInfo.KDG;

/**
 * Exception thrown when a truck arrives after its scheduled arrival window.
 */
public class LateArrivalException extends LandsideDomainException {
    public LateArrivalException() {
        super("Truck arrival is late, please book a new Appointment with %s.".formatted(KDG));
    }
}
