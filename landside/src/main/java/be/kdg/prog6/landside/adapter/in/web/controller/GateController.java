package be.kdg.prog6.landside.adapter.in.web.controller;

import be.kdg.prog6.landside.adapter.in.web.dto.TruckRecognitionDto;
import be.kdg.prog6.landside.adapter.in.web.dto.VisitDto;
import be.kdg.prog6.landside.adapter.in.web.dto.request.ConsolidateTruckAtExitGateDto;
import be.kdg.prog6.landside.adapter.in.web.dto.request.RecognizeTruckAtEntranceGateDto;
import be.kdg.prog6.landside.domain.TruckLicensePlate;
import be.kdg.prog6.landside.domain.Visit;
import be.kdg.prog6.landside.port.in.command.ConsolidateTruckAtExitGateCommand;
import be.kdg.prog6.landside.port.in.command.RecognizeTruckAtEntranceGateCommand;
import be.kdg.prog6.landside.port.in.usecase.ConsolidateTruckAtExitGateUseCase;
import be.kdg.prog6.landside.port.in.usecase.RecognizeTruckAtEntranceGateUseCase;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;
import static java.lang.String.format;

@RestController
@RequestMapping("/gates")
public class GateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GateController.class);

    private final RecognizeTruckAtEntranceGateUseCase recognizeTruckAtEntranceGateUseCase;
    private final ConsolidateTruckAtExitGateUseCase consolidateTruckAtExitGateUseCase;

    public GateController(final RecognizeTruckAtEntranceGateUseCase recognizeTruckAtEntranceGateUseCase,
                          final ConsolidateTruckAtExitGateUseCase consolidateTruckAtExitGateUseCase) {
        this.recognizeTruckAtEntranceGateUseCase = recognizeTruckAtEntranceGateUseCase;
        this.consolidateTruckAtExitGateUseCase = consolidateTruckAtExitGateUseCase;
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>truck driver</b>, I want to be recognized by my license plate so that
     * the gate opens for the truck to enter the facility.
     */
    @PostMapping("/entrance")
    @PreAuthorize("hasAnyRole('ROLE_TRUCK_DRIVER', 'ROLE_ADMIN')")
    public ResponseEntity<TruckRecognitionDto> recognizeTruckAtEntranceGate(
        @RequestBody @Valid final RecognizeTruckAtEntranceGateDto request,
        @AuthenticationPrincipal final Jwt jwt) {
        final TruckLicensePlate truckLicensePlate = new TruckLicensePlate(request.truckLicensePlate());
        logUserActivity(LOGGER, jwt, format("attempting to enter facility with Truck %s",
            truckLicensePlate
        ));
        final RecognizeTruckAtEntranceGateCommand command = new RecognizeTruckAtEntranceGateCommand(
            truckLicensePlate
        );
        // Recognize the truck and get the assigned weighbridge number
        final String assignedWBNr = recognizeTruckAtEntranceGateUseCase.recognizeTruckAndAssignWeighBridge(command);

        final TruckRecognitionDto truckRecognitionDto = new TruckRecognitionDto(
            truckLicensePlate.value(),
            assignedWBNr
        );
        return ResponseEntity.ok(truckRecognitionDto);
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>truck driver</b>, I want to be recognized by my license plate so that
     * the gate opens for exit and the seller's appointment is consolidated.
     */
    @PostMapping("/exit")
    @PreAuthorize("hasAnyRole('ROLE_TRUCK_DRIVER', 'ROLE_ADMIN')")
    public ResponseEntity<VisitDto> consolidateTruckAtExitGate(
        @RequestBody @Valid final ConsolidateTruckAtExitGateDto request,
        @AuthenticationPrincipal final Jwt jwt
    ) {
        final TruckLicensePlate truckLicensePlate = new TruckLicensePlate(request.truckLicensePlate());
        logUserActivity(LOGGER, jwt, format("attempting to depart facility with Truck %s",
            truckLicensePlate
        ));
        final ConsolidateTruckAtExitGateCommand command = new ConsolidateTruckAtExitGateCommand(
            truckLicensePlate
        );
        final Visit visit = consolidateTruckAtExitGateUseCase.consolidateTruck(command);
        return ResponseEntity.ok(VisitDto.fromDomain(visit));
    }
}
