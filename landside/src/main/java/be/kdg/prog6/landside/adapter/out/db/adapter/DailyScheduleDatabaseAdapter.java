package be.kdg.prog6.landside.adapter.out.db.adapter;

import be.kdg.prog6.landside.adapter.out.db.entity.AppointmentJpaEntity;
import be.kdg.prog6.landside.adapter.out.db.entity.DailyScheduleJpaEntity;
import be.kdg.prog6.landside.adapter.out.db.entity.TimeSlotJpaEntity;
import be.kdg.prog6.landside.adapter.out.db.repository.AppointmentJpaRepository;
import be.kdg.prog6.landside.adapter.out.db.repository.DailyScheduleJpaRepository;
import be.kdg.prog6.landside.domain.*;
import be.kdg.prog6.landside.port.out.LoadDailySchedulePort;
import be.kdg.prog6.landside.port.out.UpdateDailySchedulePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DailyScheduleDatabaseAdapter implements LoadDailySchedulePort, UpdateDailySchedulePort {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyScheduleDatabaseAdapter.class);

    private final DailyScheduleJpaRepository dailyScheduleJpaRepository;
    private final AppointmentJpaRepository appointmentJpaRepository;
    private final Clock clock;

    public DailyScheduleDatabaseAdapter(final DailyScheduleJpaRepository dailyScheduleJpaRepository,
                                        final AppointmentJpaRepository appointmentJpaRepository,
                                        final Clock clock) {
        this.dailyScheduleJpaRepository = dailyScheduleJpaRepository;
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.clock = clock;
    }

    @Override
    public Optional<DailySchedule> loadDailyScheduleByDate(final LocalDate date) {
        if (date.isBefore(LocalDate.now(clock))) {
            // Past date: return existing schedule if found
            return dailyScheduleJpaRepository.findById(date).map(this::toDailySchedule);
        }
        // Today or future: create if missing
        final DailyScheduleJpaEntity entity = dailyScheduleJpaRepository.findById(date).orElseGet(
            () -> createAndSaveDailySchedule(date)
        );
        return Optional.of(toDailySchedule(entity));
    }

    private DailyScheduleJpaEntity createAndSaveDailySchedule(final LocalDate date) {
        LOGGER.info("Creating new Daily Schedule for date {}", date);
        return dailyScheduleJpaRepository.save(new DailyScheduleJpaEntity(date));
    }

    @Override
    public Optional<DailySchedule> loadDailyScheduleForAppointmentId(final AppointmentId appointmentId) {
        final UUID id = appointmentId.id();
        final AppointmentJpaEntity appointmentJpa = appointmentJpaRepository.findById(id).orElseThrow();
        final DailyScheduleJpaEntity scheduleJpa = appointmentJpa.getTimeSlot().getDailySchedule();
        return Optional.of(toDailySchedule(scheduleJpa));
    }

    @Override
    public void updateDailySchedule(final DailySchedule dailySchedule) {
        final LocalDate date = dailySchedule.getDate();
        final DailyScheduleJpaEntity dailyScheduleJpa = dailyScheduleJpaRepository.findById(date).orElseThrow();
        LOGGER.info("Updating Daily Schedule for date {}", date);
        dailyScheduleJpa.setTimeSlots(
            dailySchedule.getTimeSlots()
                .stream()
                .map(this::toTimeSlotJpaEntity)
                .collect(Collectors.toList())
        );
        dailyScheduleJpaRepository.save(dailyScheduleJpa);
    }

    private DailySchedule toDailySchedule(final DailyScheduleJpaEntity entity) {
        final LocalDate date = entity.getDate();
        final List<TimeSlot> timeSlots = entity.getTimeSlots()
            .stream()
            .map(this::toTimeSlot)
            .collect(Collectors.toList());
        return timeSlots.isEmpty() ? new DailySchedule(date, LocalDateTime.now(clock)) : new DailySchedule(date, timeSlots);
    }

    private TimeSlot toTimeSlot(final TimeSlotJpaEntity entity) {
        final List<Appointment> appointments = entity.getAppointments()
            .stream()
            .map(this::toAppointment)
            .collect(Collectors.toList());
        return new TimeSlot(
            TimeSlotId.of(entity.getId()),
            entity.getStartTime(),
            entity.getEndTime(),
            appointments
        );
    }

    private Appointment toAppointment(final AppointmentJpaEntity entity) {
        return new Appointment(
            AppointmentId.of(entity.getAppointmentId()),
            SupplierId.of(entity.getSupplierId()),
            WarehouseId.of(entity.getWarehouseId()),
            new TruckLicensePlate(entity.getTruckLicensePlate()),
            entity.getRawMaterial(),
            entity.getArrivalWindowStart(),
            entity.getStatus()
        );
    }

    private TimeSlotJpaEntity toTimeSlotJpaEntity(final TimeSlot timeSlot) {
        final TimeSlotJpaEntity timeSlotJpaEntity = new TimeSlotJpaEntity();
        timeSlotJpaEntity.setId(timeSlot.getId().id());
        timeSlotJpaEntity.setStartTime(timeSlot.getStartTime());
        timeSlotJpaEntity.setEndTime(timeSlot.getEndTime());
        timeSlotJpaEntity.setAvailableCapacity(timeSlot.calculateAvailableCapacity());
        timeSlotJpaEntity.setAppointments(
            timeSlot.getAppointments()
                .stream()
                .map(a -> toAppointmentJpaEntity(a, timeSlotJpaEntity))
                .collect(Collectors.toList())
        );
        return timeSlotJpaEntity;
    }

    private AppointmentJpaEntity toAppointmentJpaEntity(final Appointment appointment, final TimeSlotJpaEntity parentSlot) {
        final AppointmentJpaEntity entity = new AppointmentJpaEntity();

        entity.setAppointmentId(appointment.getAppointmentId().id());
        entity.setSupplierId(appointment.getSupplierId().id());
        entity.setWarehouseId(appointment.getWarehouseId().id());
        entity.setTruckLicensePlate(appointment.getTruckLicensePlate().value());
        entity.setRawMaterial(appointment.getRawMaterial());
        entity.setArrivalWindowStart(appointment.getArrivalWindowStart());
        entity.setArrivalWindowEnd(appointment.getArrivalWindowEnd());
        entity.setStatus(appointment.getStatus());
        entity.setTimeSlot(parentSlot);

        return entity;
    }
}