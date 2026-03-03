package be.kdg.prog6.landside.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record TimeSlotId(UUID id) {
    public TimeSlotId {
        requireNonNull(id, "TimeSlotId cannot be null");
    }

    public static TimeSlotId of(final UUID id) {
        return new TimeSlotId(id);
    }

    public static TimeSlotId newId() {
        return new TimeSlotId(UUID.randomUUID());
    }
}
