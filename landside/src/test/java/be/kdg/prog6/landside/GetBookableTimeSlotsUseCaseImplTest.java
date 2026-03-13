package be.kdg.prog6.landside;

import be.kdg.prog6.landside.core.GetBookableTimeSlotsUseCaseImpl;
import be.kdg.prog6.landside.domain.DailySchedule;
import be.kdg.prog6.landside.domain.TimeSlot;
import be.kdg.prog6.landside.port.in.usecase.query.readmodel.BookableTimeSlot;
import be.kdg.prog6.landside.port.out.LoadDailySchedulePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetBookableTimeSlotsUseCaseImplTest {
    private GetBookableTimeSlotsUseCaseImpl sut;
    private LoadDailySchedulePort loadDailySchedulePort;

    private static final LocalDate TEST_DATE = LocalDate.of(
        2025, 8, 30
    );
    private static final LocalTime SLOT_BASE_START = LocalTime.of(
        6, 0
    );
    private static final LocalTime SLOT_1_START = SLOT_BASE_START;
    private static final LocalTime SLOT_1_END = SLOT_1_START.plusHours(1);

    private static final LocalTime SLOT_2_START = SLOT_1_END;
    private static final LocalTime SLOT_2_END = SLOT_2_START.plusHours(1);

    private static final LocalTime SLOT_3_START = SLOT_2_END;
    private static final LocalTime SLOT_3_END = SLOT_3_START.plusHours(1);

    private static final LocalDateTime FIXED_NOW = LocalDateTime.of(
        TEST_DATE,
        SLOT_2_START.plusMinutes(30) // during slot 2: after slot 1 ends, before slot 2 ends
    );
    private static final Clock FIXED_CLOCK = Clock.fixed(
        FIXED_NOW.toInstant(ZoneOffset.UTC),
        ZoneOffset.UTC
    );

    @BeforeEach
    void setUp() {
        loadDailySchedulePort = mock(LoadDailySchedulePort.class);
        sut = new GetBookableTimeSlotsUseCaseImpl(
            loadDailySchedulePort,
            FIXED_CLOCK
        );
    }

    @Test
    void shouldReturnOnlyTimeSlotsThatEndAfterNow() {
        // Arrange
        final DailySchedule schedule = createScheduleWithPastOngoingAndUpcomingTimeSlots();
        when(loadDailySchedulePort.loadDailyScheduleByDate(TEST_DATE)).thenReturn(
            Optional.of(schedule)
        );

        // Act
        final List<BookableTimeSlot> bookableTimeSlots = sut.getBookableTimeSlotsFor(TEST_DATE);

        // Assert
        assertEquals(2, bookableTimeSlots.size());

        assertEquals(TEST_DATE.atTime(SLOT_2_START), bookableTimeSlots.get(0).startTime());
        assertEquals(TEST_DATE.atTime(SLOT_3_START), bookableTimeSlots.get(1).startTime());
    }

    private static DailySchedule createScheduleWithPastOngoingAndUpcomingTimeSlots() {
        return new DailySchedule(TEST_DATE, List.of(
            createTimeSlot(SLOT_1_START, SLOT_1_END), // ends before now (past = not bookable)
            createTimeSlot(SLOT_2_START, SLOT_2_END), // ends after now (ongoing)
            createTimeSlot(SLOT_3_START, SLOT_3_END)  // ends after now (future)
        ));
    }

    private static TimeSlot createTimeSlot(final LocalTime start, final LocalTime end) {
        return new TimeSlot(
            TEST_DATE.atTime(start),
            TEST_DATE.atTime(end)
        );
    }
}