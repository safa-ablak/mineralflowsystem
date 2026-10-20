package be.kdg.prog6.waterside.domain.exception.bunkering;

import be.kdg.prog6.waterside.domain.exception.WatersideDomainException;

public class BunkeringLimitExceededException extends WatersideDomainException {
    public BunkeringLimitExceededException(final int limit) {
        super(
            "Cannot perform more than %d Bunkering Operations in a single day."
                .formatted(limit)
        );
    }
}
