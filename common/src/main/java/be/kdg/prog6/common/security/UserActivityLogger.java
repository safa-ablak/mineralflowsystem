package be.kdg.prog6.common.security;

import org.slf4j.Logger;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Utility for consistent logging of user activities across bounded contexts.
 * <p>
 * Example:
 * <pre>
 *     UserActivityLogger.logUserActivity(LOGGER, jwt, "is viewing all Raw Materials");
 * </pre>
 */
public final class UserActivityLogger {
    private UserActivityLogger() {
        throw new AssertionError("Utility class");
    }

    /**
     * Logs a user activity in a consistent format: "email (role) action". <br>
     *
     * <p>
     * Since all endpoints in this project are protected by OAuth2 Resource Server
     * configuration and require authentication, {@code jwt} is always non-null.
     * </p>
     *
     * @param logger The SLF4J logger to write the log entry.
     * @param jwt    The JWT token of the authenticated user.
     * @param action Description of the user action being logged.
     */
    public static void logUserActivity(final Logger logger,
                                       final Jwt jwt,
                                       final String action) {
        final UserRole role = UserRoleUtil.extractRole(jwt.getClaims());
        final Object email = jwt.getClaims().get("email");
        logger.info("{} ({}) {}", email, role.getDisplayName(), action);
    }
}
