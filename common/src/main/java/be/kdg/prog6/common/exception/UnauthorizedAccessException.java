package be.kdg.prog6.common.exception;

import java.util.UUID;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(final UUID authenticatedUserId, final UUID resourceOwnerId) {
        super(
            "User %s is not authorized to access resource owned by %s".formatted(authenticatedUserId, resourceOwnerId)
        );
    }
}
