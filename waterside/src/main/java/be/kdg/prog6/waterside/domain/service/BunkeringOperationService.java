package be.kdg.prog6.waterside.domain.service;

import be.kdg.prog6.waterside.domain.exception.bunkering.BunkeringLimitExceededException;

/**
 * A domain service for handling Bunkering Operations
 *
 */
public class BunkeringOperationService {
    private static final int MAX_BUNKERING_OPERATIONS_PER_DAY = 6;

    /**
     * Validates whether the number of bunkering operations for a given day
     * exceeds the allowed maximum limit. (Used for API calls)
     *
     * @param dailyOperationCount The total number of bunkering operations already performed on the given day.
     * @throws BunkeringLimitExceededException if the maximum daily limit is exceeded.
     */
    public void validateBunkeringLimit(final int dailyOperationCount) {
        if (!canPerformBunkering(dailyOperationCount)) {
            throw BunkeringLimitExceededException.forLimit(dailyOperationCount);
        }
    }

    /**
     * Checks whether a bunkering operation can still be performed. (Used for the scheduled task)
     *
     * @param dailyOperationCount The current number of performed bunkering operations.
     * @return true if more operations can be performed, false otherwise.
     */
    public boolean canPerformBunkering(final int dailyOperationCount) {
        return dailyOperationCount < MAX_BUNKERING_OPERATIONS_PER_DAY;
    }
}
