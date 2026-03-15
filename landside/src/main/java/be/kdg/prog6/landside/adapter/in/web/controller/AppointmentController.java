package be.kdg.prog6.landside.adapter.in.web.controller;

import be.kdg.prog6.landside.adapter.in.web.dto.AppointmentDto;
import be.kdg.prog6.landside.adapter.in.web.dto.BookableTimeSlotDto;
import be.kdg.prog6.landside.adapter.in.web.dto.request.MakeAppointmentDto;
import be.kdg.prog6.landside.domain.*;
import be.kdg.prog6.landside.port.in.command.MakeAppointmentCommand;
import be.kdg.prog6.landside.port.in.usecase.MakeAppointmentUseCase;
import be.kdg.prog6.landside.port.in.usecase.query.GetBookableTimeSlotsUseCase;
import be.kdg.prog6.landside.port.in.usecase.query.readmodel.BookableTimeSlot;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;
import static be.kdg.prog6.common.security.UserRole.SELLER;
import static be.kdg.prog6.common.security.UserRoleUtil.hasRole;
import static java.lang.String.format;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentController.class);

    private final MakeAppointmentUseCase makeAppointmentUseCase;
    private final GetBookableTimeSlotsUseCase getBookableTimeSlotsUseCase;

    public AppointmentController(final MakeAppointmentUseCase makeAppointmentUseCase,
                                 final GetBookableTimeSlotsUseCase getBookableTimeSlotsUseCase) {
        this.makeAppointmentUseCase = makeAppointmentUseCase;
        this.getBookableTimeSlotsUseCase = getBookableTimeSlotsUseCase;
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>seller</b>, I want to make an appointment for a truck loaded with a specific raw material
     * so that the truck can enter the facility during a specified arrival window.
     * <br></br>There's a limit to the number of appointments per time slot. See {@link TimeSlot} for more information.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
    public ResponseEntity<AppointmentDto> makeAppointment(
        @RequestBody @Valid final MakeAppointmentDto request,
        @AuthenticationPrincipal final Jwt jwt
    ) {
        final SupplierId supplierId = hasRole(jwt, SELLER)
            ? SupplierId.of(UUID.fromString(jwt.getSubject()))
            : SupplierId.of(request.supplierId());
        logUserActivity(LOGGER, jwt, "is making an Appointment");

        final MakeAppointmentCommand command = new MakeAppointmentCommand(
            supplierId,
            new TruckLicensePlate(request.truckLicensePlate()),
            RawMaterial.fromString(request.rawMaterial()),
            request.scheduledArrivalTime()
        );
        final Appointment appointment = makeAppointmentUseCase.makeAppointment(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(AppointmentDto.fromDomain(appointment));
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>seller</b>, I want to see the bookable time slots for a specific day to see if I can book an appointment.
     */
    @GetMapping("/bookable-time-slots")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_ADMIN')")
    public ResponseEntity<List<BookableTimeSlotDto>> getBookableTimeSlotsByDate(
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date,
        @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is viewing Bookable Time Slots for date %s",
            date
        ));
        final List<BookableTimeSlot> bookableTimeSlots = getBookableTimeSlotsUseCase.getBookableTimeSlotsFor(
            date
        );
        final List<BookableTimeSlotDto> bookableTimeSlotDtos = bookableTimeSlots
            .stream()
            .map(BookableTimeSlotDto::from)
            .toList();
        return ResponseEntity.ok(bookableTimeSlotDtos);
    }
}