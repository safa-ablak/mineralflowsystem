package be.kdg.prog6.landside;

import be.kdg.prog6.landside.core.CancelMismatchedAppointmentsForWarehouseUseCaseImpl;
import be.kdg.prog6.landside.domain.*;
import be.kdg.prog6.landside.port.in.command.CancelMismatchedAppointmentsForWarehouseCommand;
import be.kdg.prog6.landside.port.out.AppointmentQueryPort;
import be.kdg.prog6.landside.port.out.LoadDailySchedulePort;
import be.kdg.prog6.landside.port.out.UpdateDailySchedulePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CancelMismatchedAppointmentsForWarehouseUseCaseImplMockingTest {
    private CancelMismatchedAppointmentsForWarehouseUseCaseImpl sut;
    private AppointmentQueryPort appointmentQueryPort;
    private LoadDailySchedulePort loadDailySchedulePort;
    private UpdateDailySchedulePort updateDailySchedulePort;

    private static final LocalDateTime FUTURE_ARRIVAL = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0);

    @BeforeEach
    void setUp() {
        appointmentQueryPort = mock(AppointmentQueryPort.class);
        loadDailySchedulePort = mock(LoadDailySchedulePort.class);
        updateDailySchedulePort = mock(UpdateDailySchedulePort.class);
        sut = new CancelMismatchedAppointmentsForWarehouseUseCaseImpl(
            appointmentQueryPort,
            loadDailySchedulePort,
            updateDailySchedulePort
        );
    }

    @Test
    void shouldCancelAppointmentsWithMismatchedRawMaterial() {
        // Arrange
        final Appointment gypsumAppointment = createScheduledAppointment(RawMaterial.GYPSUM, FUTURE_ARRIVAL);
        final Appointment ironOreAppointment = createScheduledAppointment(RawMaterial.IRON_ORE, FUTURE_ARRIVAL);

        when(appointmentQueryPort.loadAppointmentsByStatusAndWarehouseId(AppointmentStatus.SCHEDULED, TestIds.WAREHOUSE_ID))
            .thenReturn(List.of(gypsumAppointment, ironOreAppointment));

        final DailySchedule schedule = createScheduleWithAppointments(
            FUTURE_ARRIVAL.toLocalDate(), List.of(gypsumAppointment, ironOreAppointment)
        );
        when(loadDailySchedulePort.loadDailyScheduleByDate(FUTURE_ARRIVAL.toLocalDate()))
            .thenReturn(Optional.of(schedule));

        final CancelMismatchedAppointmentsForWarehouseCommand command =
            new CancelMismatchedAppointmentsForWarehouseCommand(TestIds.WAREHOUSE_ID, RawMaterial.IRON_ORE);

        // Act
        sut.cancelAppointmentsWithMismatchedRawMaterial(command);

        // Assert - only the GYPSUM appointment should be cancelled (mismatched with new IRON_ORE)
        assertTrue(gypsumAppointment.isCancelled());
        assertTrue(ironOreAppointment.isScheduled());

        final ArgumentCaptor<DailySchedule> scheduleCaptor = ArgumentCaptor.forClass(DailySchedule.class);
        verify(updateDailySchedulePort).updateDailySchedule(scheduleCaptor.capture());
        assertEquals(FUTURE_ARRIVAL.toLocalDate(), scheduleCaptor.getValue().getDate());
    }

    @Test
    void shouldNotUpdateScheduleWhenNoMismatchedAppointments() {
        // Arrange
        final Appointment ironOreAppointment = createScheduledAppointment(RawMaterial.IRON_ORE, FUTURE_ARRIVAL);

        when(appointmentQueryPort.loadAppointmentsByStatusAndWarehouseId(AppointmentStatus.SCHEDULED, TestIds.WAREHOUSE_ID))
            .thenReturn(List.of(ironOreAppointment));

        final CancelMismatchedAppointmentsForWarehouseCommand command =
            new CancelMismatchedAppointmentsForWarehouseCommand(TestIds.WAREHOUSE_ID, RawMaterial.IRON_ORE);

        // Act
        sut.cancelAppointmentsWithMismatchedRawMaterial(command);

        // Assert - no mismatches, so no schedule loading or updating should occur
        assertTrue(ironOreAppointment.isScheduled());
        verifyNoInteractions(loadDailySchedulePort);
        verifyNoInteractions(updateDailySchedulePort);
    }

    @Test
    void shouldCancelAppointmentsAcrossMultipleDays() {
        // Arrange
        final LocalDateTime day1Arrival = FUTURE_ARRIVAL;
        final LocalDateTime day2Arrival = FUTURE_ARRIVAL.plusDays(1);

        final Appointment day1Appointment = createScheduledAppointment(RawMaterial.GYPSUM, day1Arrival);
        final Appointment day2Appointment = createScheduledAppointment(RawMaterial.CEMENT, day2Arrival);

        when(appointmentQueryPort.loadAppointmentsByStatusAndWarehouseId(AppointmentStatus.SCHEDULED, TestIds.WAREHOUSE_ID))
            .thenReturn(List.of(day1Appointment, day2Appointment));

        final DailySchedule day1Schedule = createScheduleWithAppointments(
            day1Arrival.toLocalDate(), List.of(day1Appointment)
        );
        final DailySchedule day2Schedule = createScheduleWithAppointments(
            day2Arrival.toLocalDate(), List.of(day2Appointment)
        );
        when(loadDailySchedulePort.loadDailyScheduleByDate(day1Arrival.toLocalDate()))
            .thenReturn(Optional.of(day1Schedule));
        when(loadDailySchedulePort.loadDailyScheduleByDate(day2Arrival.toLocalDate()))
            .thenReturn(Optional.of(day2Schedule));

        final CancelMismatchedAppointmentsForWarehouseCommand command =
            new CancelMismatchedAppointmentsForWarehouseCommand(TestIds.WAREHOUSE_ID, RawMaterial.IRON_ORE);

        // Act
        sut.cancelAppointmentsWithMismatchedRawMaterial(command);

        // Assert - both should be cancelled since neither matches IRON_ORE
        assertTrue(day1Appointment.isCancelled());
        assertTrue(day2Appointment.isCancelled());
        verify(updateDailySchedulePort, times(2)).updateDailySchedule(any(DailySchedule.class));
    }

    private static Appointment createScheduledAppointment(final RawMaterial rawMaterial, final LocalDateTime arrivalTime) {
        return new Appointment(
            AppointmentId.newId(),
            TestIds.SUPPLIER_ID,
            TestIds.WAREHOUSE_ID,
            TestIds.TRUCK_LICENSE_PLATE,
            rawMaterial,
            arrivalTime,
            AppointmentStatus.SCHEDULED
        );
    }

    private static DailySchedule createScheduleWithAppointments(final LocalDate date, final List<Appointment> appointments) {
        final LocalDateTime slotStart = date.atTime(FUTURE_ARRIVAL.toLocalTime());
        final TimeSlot timeSlot = new TimeSlot(
            TimeSlotId.newId(),
            slotStart,
            slotStart.plusHours(1),
            appointments
        );
        return new DailySchedule(date, List.of(timeSlot));
    }
}
