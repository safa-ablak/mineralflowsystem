package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.DailySchedule;
import be.kdg.prog6.landside.domain.TimeSlot;
import be.kdg.prog6.landside.domain.exception.DailyScheduleNotAvailableException;
import be.kdg.prog6.landside.port.in.usecase.query.GetBookableTimeSlotsUseCase;
import be.kdg.prog6.landside.port.in.usecase.query.readmodel.BookableTimeSlot;
import be.kdg.prog6.landside.port.out.LoadDailySchedulePort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetBookableTimeSlotsUseCaseImpl implements GetBookableTimeSlotsUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetBookableTimeSlotsUseCaseImpl.class);

    private final LoadDailySchedulePort loadDailySchedulePort;
    private final Clock clock;

    public GetBookableTimeSlotsUseCaseImpl(final LoadDailySchedulePort loadDailySchedulePort,
                                           final Clock clock) {
        this.loadDailySchedulePort = loadDailySchedulePort;
        this.clock = clock;
    }

    /**
     * Time slots are not persisted when merely displaying bookability.<br>
     * They are only saved to the database when an appointment is created.<br>
     * See {@link be.kdg.prog6.landside.adapter.in.web.controller.AppointmentController} for the booking logic.
     */
    @Override
    @Transactional
    public List<BookableTimeSlot> getBookableTimeSlotsFor(final LocalDate date) {
        LOGGER.info("Getting Bookable Time Slots for date {} at {}", date, KDG);
        final DailySchedule dailySchedule = loadDailySchedulePort.loadDailyScheduleByDate(date).orElseThrow(
            () -> DailyScheduleNotAvailableException.forDate(date)
        );
        return dailySchedule.getTimeSlots()
            .stream()
            .filter(slot -> slot.isBookableAt(LocalDateTime.now(clock)))
            .sorted(Comparator.comparing(TimeSlot::getStartTime))
            .map(BookableTimeSlot::fromDomain)
            .toList();
    }
}