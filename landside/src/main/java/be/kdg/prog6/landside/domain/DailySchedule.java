package be.kdg.prog6.landside.domain;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.landside.domain.exception.AppointmentNotFoundException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DailySchedule {
    private static final Duration DEFAULT_TIME_SLOT_DURATION = Duration.ofMinutes(60); // Default for all, but the last slot of the day

    /// Alongside the date field, DailyScheduleId could be added as well for testing purposes
    private final LocalDate date; // Natural identifier, it won't ever change.
    private final List<TimeSlot> timeSlots;

    public DailySchedule(final LocalDate date, final LocalDateTime now) {
        if (date.isBefore(now.toLocalDate())) {
            throw new InvalidOperationException("Cannot create a schedule for a past date.");
        }
        this.date = date;
        timeSlots = createTimeSlots(this.date, now);
    }

    public DailySchedule(final LocalDate date, final List<TimeSlot> timeSlots) {
        this.date = date;
        this.timeSlots = timeSlots;
    }

    public Appointment makeAppointment(final SupplierId supplierId, final TruckLicensePlate truckLicensePlate,
                                       final RawMaterial rawMaterial, final LocalDateTime scheduledArrivalTime,
                                       final WarehouseId warehouseId, final LocalDateTime now) {
        assertValidBooking(scheduledArrivalTime, now);
        final Appointment appointment = new Appointment(supplierId, truckLicensePlate, rawMaterial, scheduledArrivalTime, warehouseId);
        addAppointmentToMatchingTimeSlot(appointment);
        return appointment;
    }

    public Appointment fulfillAppointment(final AppointmentId appointmentId) {
        final Appointment appointment = findAppointmentById(appointmentId).orElseThrow(
            () -> AppointmentNotFoundException.forId(appointmentId)
        );
        appointment.fulfill();
        return appointment;
    }

    public void cancelAppointment(final AppointmentId appointmentId) {
        final Appointment appointment = findAppointmentById(appointmentId).orElseThrow(
            () -> AppointmentNotFoundException.forId(appointmentId)
        );
        appointment.cancel();
    }

    private void assertValidBooking(final LocalDateTime scheduledArrivalTime, final LocalDateTime now) {
        if (!isSameDateAs(scheduledArrivalTime)) {
            throw new InvalidOperationException("Invalid date. Must be the same as the schedule.");
        }
        if (isInPast(scheduledArrivalTime, now)) {
            throw new InvalidOperationException("Arrival time cannot be in the past.");
        }
        if (!hasAvailableSlotFor(scheduledArrivalTime)) {
            throw new InvalidOperationException("The corresponding time slot for the selected arrival time is unavailable.");
        }
    }

    private boolean hasAvailableSlotFor(final LocalDateTime scheduledArrivalTime) {
        for (TimeSlot slot : timeSlots) {
            if (slot.contains(scheduledArrivalTime) && slot.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    private boolean isSameDateAs(final LocalDateTime scheduledArrivalTime) {
        return scheduledArrivalTime.toLocalDate().equals(this.date);
    }

    private boolean isInPast(final LocalDateTime scheduledArrivalTime, final LocalDateTime now) {
        return scheduledArrivalTime.isBefore(now);
    }

    private void addAppointmentToMatchingTimeSlot(final Appointment appointment) {
        for (TimeSlot slot : timeSlots) {
            if (slot.contains(appointment.getArrivalWindowStart())) {
                slot.addAppointment(appointment);
                return;
            }
        }
        throw new InvalidOperationException("No valid time slot found for this appointment");
    }

    public LocalDate getDate() {
        return date;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    // Note: We were told that we should assume the same truck doesn't arrive twice during the same slot or within its arrival window.
    //  If the appointment is not found, return an empty optional.
    public Optional<Appointment> findAppointmentByTruckAndTime(
        final TruckLicensePlate plate,
        final LocalDateTime time
    ) {
        for (TimeSlot slot : timeSlots) {
            if (slot.contains(time)) {
                for (Appointment appointment : slot.getAppointments()) {
                    if (appointment.getTruckLicensePlate().equals(plate)) {
                        return Optional.of(appointment);
                    }
                }
            }
        }
        return Optional.empty();
    }

    public List<Appointment> findAllAppointments() {
        final List<Appointment> allAppointments = new ArrayList<>();
        for (TimeSlot timeSlot : timeSlots) {
            allAppointments.addAll(timeSlot.getAppointments());
        }
        return allAppointments;
    }

    public Optional<Appointment> findAppointmentById(final AppointmentId appointmentId) {
        for (TimeSlot timeSlot : timeSlots) {
            for (Appointment appointment : timeSlot.getAppointments()) {
                if (appointment.getAppointmentId().equals(appointmentId)) {
                    return Optional.of(appointment);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Creates a list of 1-hour {@link TimeSlot}s for the given date.
     * <p>
     * - If the date is today, the first slot starts at the current hour (rounded down).<br>
     * - Otherwise, slots start at midnight of the given date.<br>
     * - Slots are created until midnight of the next day, and the final slot may be shorter than 1 hour
     * if the day ends mid-slot.<br>
     * <p>
     * Used by {@link DailySchedule} to build the appointment capacity for a full day.
     *
     * @param date The date for which time slots should be generated.
     * @return A list of non-overlapping {@code TimeSlot}s for the given day.
     */
    private List<TimeSlot> createTimeSlots(final LocalDate date, final LocalDateTime now) {
        final List<TimeSlot> slots = new ArrayList<>();
        final boolean isForToday = date.equals(now.toLocalDate());

        LocalDateTime startTime = isForToday ? now.truncatedTo(ChronoUnit.HOURS) : date.atStartOfDay();
        final LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        while (startTime.isBefore(endOfDay)) {
            LocalDateTime endTime = startTime.plus(DEFAULT_TIME_SLOT_DURATION);
            if (endTime.isAfter(endOfDay)) {
                endTime = endOfDay;
            }
            slots.add(new TimeSlot(startTime, endTime));
            startTime = endTime;
        }
        return slots;
    }
}
