package be.kdg.prog6.landside.port.in.query;

import be.kdg.prog6.common.exception.InvalidOperationException;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

public record GetTruckOverviewsQuery(
    LocalDate from,
    LocalDate to
) {
    public GetTruckOverviewsQuery {
        requireNonNull(from);
        requireNonNull(to);
        if (from.isAfter(to)) {
            throw new InvalidOperationException("The '%s' date must be before or equal to the '%s' date"
                .formatted(from, to)
            );
        }
    }
}
