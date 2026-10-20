package be.kdg.prog6.warehousing.core.policy;

import be.kdg.prog6.common.exception.UnauthorizedAccessException;
import be.kdg.prog6.common.security.UserRole;
import be.kdg.prog6.common.security.UserRoleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Enforces the resource access policy: users can only access their own resources.
 * Admins always bypass the ownership check. Warehouse managers may bypass for read operations.
 */
@Service
public class ResourceAccessPolicy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceAccessPolicy.class);

    public void enforce(final Jwt jwt, final UUID resourceOwnerId, final AccessMode mode) {
        final UUID authenticatedUserId = UUID.fromString(jwt.getSubject());
        if (UserRoleUtil.isAdmin(jwt)) {
            LOGGER.info("Admin access granted for resource owned by {}", resourceOwnerId);
            return;
        }
        if (mode == AccessMode.READ && UserRoleUtil.hasRole(jwt, UserRole.WAREHOUSE_MANAGER)) {
            LOGGER.info("Warehouse Manager read access granted for resource owned by {}", resourceOwnerId);
            return;
        }
        if (!authenticatedUserId.equals(resourceOwnerId)) {
            LOGGER.warn("User {} attempted to access resource owned by {}", authenticatedUserId, resourceOwnerId);
            throw new UnauthorizedAccessException(authenticatedUserId, resourceOwnerId);
        }
    }
}
