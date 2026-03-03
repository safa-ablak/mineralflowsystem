package be.kdg.prog6.landside.domain;

/**
 * Enum representing the various statuses of a truck appointment within the system.
 */
public enum AppointmentStatus {
    /**
     * The initial state when an appointment is successfully scheduled.
     */
    SCHEDULED,
    /**
     * (Not Implemented) When a truck appointment is cancelled by the supplier who scheduled the appointment or by an admin.
     */
    CANCELLED,
    /**
     * The appointment has been successfully fulfilled, meaning the truck has arrived, completed its visit, and left the facility.
     */
    FULFILLED
}