package be.kdg.prog6.landside.adapter.out.db.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(catalog = "landside", name = "time_slots")
public class TimeSlotJpaEntity {
    @Id
    @Column(name = "id", columnDefinition = "varchar(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL)
    private List<AppointmentJpaEntity> appointments = new ArrayList<>();

    @Column(name = "available_capacity", nullable = false)
    private int availableCapacity;

    @ManyToOne
    @JoinColumn(name = "daily_schedule_date", referencedColumnName = "date")
    private DailyScheduleJpaEntity dailySchedule;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(final LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(final LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<AppointmentJpaEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(final List<AppointmentJpaEntity> appointments) {
        this.appointments = appointments;
    }

    public DailyScheduleJpaEntity getDailySchedule() {
        return dailySchedule;
    }

    public void setDailySchedule(final DailyScheduleJpaEntity dailySchedule) {
        this.dailySchedule = dailySchedule;
    }

    public void setAvailableCapacity(final int availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public int getAvailableCapacity() {
        return availableCapacity;
    }
}
