package be.kdg.prog6.landside.domain;

import be.kdg.prog6.common.exception.InvalidOperationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlot {
    private static final int MAX_APPOINTMENTS = 40;

    private final TimeSlotId id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final List<Appointment> appointments;

    public TimeSlot(final LocalDateTime startTime, final LocalDateTime endTime) {
        this.id = TimeSlotId.newId();
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointments = new ArrayList<>();
    }

    public TimeSlot(final TimeSlotId id, final LocalDateTime startTime, final LocalDateTime endTime, final List<Appointment> appointments) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointments = appointments;
    }

    public TimeSlotId getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * True if this slot can still be booked at the given moment.
     */
    public boolean isBookableAt(final LocalDateTime now) {
        // Past/ongoing policy: bookable if it ends in the future.
        // If you want strictly future-only (=starting from the very next slot), use startTime.isAfter(now).
        return endTime.isAfter(now) && isAvailable();
    }

    // Get all active appointments (exclude CANCELLED)
    public List<Appointment> findActiveAppointments() {
        return appointments.stream()
            .filter(Appointment::isActive)
            .toList();
    }

    // Add appointment to the time slot, only if there is available capacity
    public void addAppointment(final Appointment appointment) {
        if (!isAvailable()) {
            throw new InvalidOperationException("No available capacity in this Time Slot");
        }
        appointments.add(appointment);
    }

    // Method to check if a given time is within this time slot
    public boolean contains(final LocalDateTime time) {
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }

    // Get the number of active appointments (excluding CANCELLED)
    public int countActiveAppointments() {
        return (int) appointments
            .stream()
            .filter(Appointment::isActive)
            .count();
    }

    // Method to check if the time slot is available
    public boolean isAvailable() {
        return countActiveAppointments() < MAX_APPOINTMENTS;
    }

    public boolean isFullyBooked() {
        return countActiveAppointments() == MAX_APPOINTMENTS;
    }

    public int calculateAvailableCapacity() {
        return MAX_APPOINTMENTS - countActiveAppointments();
    }
}
