package be.kdg.prog6.landside.domain;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record AppointmentId(UUID id) {
    public AppointmentId {
        requireNonNull(id, "AppointmentId cannot be null");
    }

    public static AppointmentId of(final UUID id) {
        return new AppointmentId(id);
    }

    public static AppointmentId newId() {
        return new AppointmentId(UUID.randomUUID());
    }
}
