package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.DailySchedule;
import be.kdg.prog6.landside.domain.TimeSlot;
import be.kdg.prog6.landside.domain.exception.DailyScheduleNotAvailableException;
import be.kdg.prog6.landside.port.in.usecase.query.GetAvailableTimeSlotsUseCase;
import be.kdg.prog6.landside.port.in.usecase.query.readmodel.AvailableTimeSlot;
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
public class GetAvailableTimeSlotsUseCaseImpl implements GetAvailableTimeSlotsUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAvailableTimeSlotsUseCaseImpl.class);

    private final LoadDailySchedulePort loadDailySchedulePort;
    private final Clock clock;

    public GetAvailableTimeSlotsUseCaseImpl(final LoadDailySchedulePort loadDailySchedulePort,
                                            final Clock clock) {
        this.loadDailySchedulePort = loadDailySchedulePort;
        this.clock = clock;
    }

    /**
     * Time slots are not persisted when merely displaying availability.<br>
     * They are only saved to the database when an appointment is created.<br>
     * See {@link be.kdg.prog6.landside.adapter.in.web.controller.AppointmentController} for the booking logic.
     */
    @Override
    @Transactional
    public List<AvailableTimeSlot> getAvailableTimeSlotsFor(final LocalDate date) {
        final LocalDateTime now = LocalDateTime.now(clock);
        LOGGER.info("Getting Available Time Slots for date {} at {}", date, KDG);
        final DailySchedule dailySchedule = loadDailySchedulePort.loadDailyScheduleByDate(date).orElseThrow(
            () -> DailyScheduleNotAvailableException.forDate(date)
        );
        return dailySchedule.getTimeSlots()
            .stream()
            .filter(slot -> slot.isBookableAt(now))
            .sorted(Comparator.comparing(TimeSlot::getStartTime))
            .map(AvailableTimeSlot::fromDomain)
            .toList();
    }
}
