package be.kdg.prog6.landside.adapter.out.db.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(catalog = "landside", name = "daily_schedules")
public class DailyScheduleJpaEntity {
    @Id
    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

    /**
     * List of time slots for the day. Each time slot belongs to a daily schedule.
     * Cascade ALL to propagate changes to the time slots.
     */
    @OneToMany(mappedBy = "dailySchedule", cascade = CascadeType.ALL)
    private List<TimeSlotJpaEntity> timeSlots = new ArrayList<>();

    public DailyScheduleJpaEntity() {
    }

    public DailyScheduleJpaEntity(final LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(final LocalDate date) {
        this.date = date;
    }

    public List<TimeSlotJpaEntity> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(final List<TimeSlotJpaEntity> timeSlots) {
        timeSlots.forEach(timeSlot -> timeSlot.setDailySchedule(this));
        this.timeSlots = timeSlots;
    }
}
