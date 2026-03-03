package be.kdg.prog6.landside.domain;

/**
 * Enum representing the various stages of a truck's visit to the facility, from arrival to completion.
 */
public enum VisitStatus {
    /**
     * When the truck is recognized by its license plate at the entrance gate and entered the facility.
     */
    ENTERED_FACILITY,
    /**
     * When the truck passes the weigh bridge for the first time upon arrival.
     */
    WEIGHED_IN,
    /**
     * When the truck docks at the conveyor belt to unload its payload.
     */
    DOCKED,
    /**
     * When the truck passes the weigh bridge for the second time on its way out.
     */
    WEIGHED_OUT,
    /**
     * When the truck's license plate is scanned and consolidated during exit.
     */
    COMPLETED
}
