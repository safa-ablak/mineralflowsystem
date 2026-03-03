package be.kdg.prog6.common.security;

import java.util.List;
import java.util.Map;

public final class UserRoleUtil {
    private UserRoleUtil() {
        throw new AssertionError("Utility class");
    }

    /**
     * Extracts the user role from JWT claims and maps it to the UserRole enum
     * <b>(Assuming a user can only have one role within the Mineral Flow System).</b>
     *
     * @param claims JWT claims map.
     * @return The extracted UserRole. If no valid role is found, returns UserRole.UNKNOWN.
     */
    public static UserRole extractRole(final Map<String, Object> claims) {
        if (claims == null) {
            return UserRole.UNKNOWN;
        }
        final Object realmAccessObj = claims.get("realm_access");
        if (!(realmAccessObj instanceof Map<?, ?> accessMap)) {
            return UserRole.UNKNOWN;
        }
        final Object rolesObj = accessMap.get("roles");
        if (!(rolesObj instanceof List<?> rolesList) || rolesList.isEmpty()) {
            return UserRole.UNKNOWN;
        }
        return rolesList.stream()
            .filter(String.class::isInstance)
            .map(String.class::cast)
            .map(UserRole::fromString) // Convert to enum
            .filter(role -> role != UserRole.UNKNOWN) // Ignore unknown roles
            .findFirst() // Get the first "real role"
            .orElse(UserRole.UNKNOWN); // If no "real role" is found then return UNKNOWN
    }
}
