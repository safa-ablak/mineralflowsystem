package be.kdg.prog6.waterside.domain.exception.bunkering;

import be.kdg.prog6.waterside.domain.exception.WatersideDomainException;

public class BunkeringLimitExceededException extends WatersideDomainException {
    private BunkeringLimitExceededException(final String message) {
        super(message);
    }

    public static BunkeringLimitExceededException forLimit(final int limit) {
        return new BunkeringLimitExceededException(
            "Cannot perform more than %d Bunkering Operations in a single day."
                .formatted(limit)
        );
    }
}
