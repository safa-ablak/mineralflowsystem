package be.kdg.prog6.common.security;

import java.util.Arrays;

public enum UserRole {
    /* Real User Roles within the system */
    ADMIN("Administrator"),
    WAREHOUSE_MANAGER("Warehouse Manager"),
    ACCOUNTANT("Accountant"),
    SELLER("Seller"),
    TRUCK_DRIVER("Truck Driver"),
    BUYER("Buyer"),
    BUNKERING_OFFICER("Bunkering Officer"),
    INSPECTOR("Inspector"),
    SHIP_CAPTAIN("Ship Captain"),
    FOREMAN("Foreman"),
    /* Fallback User Role */
    UNKNOWN("Unknown"); // Fallback for unrecognized roles

    private final String displayName;

    UserRole(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets a user-friendly display name for this role.
     *
     * @return The display name of the role.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Resolves a {@link UserRole} from a string value, case-insensitive.
     * Returns {@link #UNKNOWN} if the value is null, blank, or unrecognized.
     */
    public static UserRole fromString(final String value) {
        if (value == null || value.isBlank()) {
            return UNKNOWN;
        }
        return Arrays.stream(values())
            .filter(r -> r.name().equalsIgnoreCase(value))
            .findFirst()
            .orElse(UNKNOWN);
    }
}
