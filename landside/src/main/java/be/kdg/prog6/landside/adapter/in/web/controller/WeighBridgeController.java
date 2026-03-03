package be.kdg.prog6.landside.adapter.in.web.controller;

import be.kdg.prog6.landside.adapter.in.web.dto.WeighBridgeTicketDto;
import be.kdg.prog6.landside.adapter.in.web.dto.WeighInDto;
import be.kdg.prog6.landside.adapter.in.web.dto.request.RecordWeighInDto;
import be.kdg.prog6.landside.adapter.in.web.dto.request.RecordWeighOutDto;
import be.kdg.prog6.landside.domain.TruckLicensePlate;
import be.kdg.prog6.landside.domain.WeighBridge;
import be.kdg.prog6.landside.domain.WeighBridgeTransaction;
import be.kdg.prog6.landside.port.in.command.RecordTruckWeighInCommand;
import be.kdg.prog6.landside.port.in.command.RecordTruckWeighOutCommand;
import be.kdg.prog6.landside.port.in.usecase.GetWeighBridgesUseCase;
import be.kdg.prog6.landside.port.in.usecase.RecordTruckWeighInUseCase;
import be.kdg.prog6.landside.port.in.usecase.RecordTruckWeighOutUseCase;
import be.kdg.prog6.landside.adapter.in.web.dto.WeighBridgeDto;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;
import static java.lang.String.format;

@RestController
@RequestMapping("/weighbridges")
public class WeighBridgeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeighBridgeController.class);

    private final GetWeighBridgesUseCase getWeighBridgesUseCase;
    private final RecordTruckWeighInUseCase recordTruckWeighInUseCase;
    private final RecordTruckWeighOutUseCase recordTruckWeighOutUseCase;

    public WeighBridgeController(final GetWeighBridgesUseCase getWeighBridgesUseCase,
                                 final RecordTruckWeighInUseCase recordTruckWeighInUseCase,
                                 final RecordTruckWeighOutUseCase recordTruckWeighOutUseCase) {
        this.getWeighBridgesUseCase = getWeighBridgesUseCase;
        this.recordTruckWeighInUseCase = recordTruckWeighInUseCase;
        this.recordTruckWeighOutUseCase = recordTruckWeighOutUseCase;
    }

    /**
     * 📘 - User Story<br></br>
     * As an <b>admin</b>, I want to see all weigh bridges on KdG's site, so that I can monitor weigh bridge availability.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<List<WeighBridgeDto>> getWeighBridges(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is viewing all Weigh Bridges");
        final List<WeighBridge> weighBridges = getWeighBridgesUseCase.getWeighBridges();

        final List<WeighBridgeDto> weighBridgeDtos = weighBridges.stream()
            .map(WeighBridgeDto::fromDomain)
            .toList();
        return ResponseEntity.ok(weighBridgeDtos);
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>truck driver</b>, I want to weigh my truck at the weighbridge so that the gross weight of my truck gets recorded.
     */
    @PostMapping("/weigh-in")
    @PreAuthorize("hasAnyRole('ROLE_TRUCK_DRIVER', 'ROLE_ADMIN')")
    public ResponseEntity<WeighInDto> recordWeighIn(@RequestBody @Valid final RecordWeighInDto request,
                                                    @AuthenticationPrincipal final Jwt jwt) {
        final TruckLicensePlate truckLicensePlate = new TruckLicensePlate(request.truckLicensePlate());
        logUserActivity(LOGGER, jwt, format("is at a Weigh Bridge for the weigh-in with Truck %s",
            truckLicensePlate
        ));
        final RecordTruckWeighInCommand command = new RecordTruckWeighInCommand(
            truckLicensePlate,
            request.grossWeight()
        );
        final WeighBridgeTransaction weighInTransaction = recordTruckWeighInUseCase.recordWeighIn(command);
        final WeighInDto weighInDto = WeighInDto.fromDomain(weighInTransaction);

        LOGGER.info("Created Weigh-In: {}", weighInDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(weighInDto); // WeighBridgeTransaction created -> HttpStatus.CREATED
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>truck driver</b>, I want to pass the weighing bridge again and get a Weighbridge Ticket (WBT) that includes:
     * The gross weight upon arrival, tare weight, net weight, timestamp of weighing, truck license plate value.
     */
    @PostMapping("/weigh-out")
    @PreAuthorize("hasAnyRole('ROLE_TRUCK_DRIVER', 'ROLE_ADMIN')")
    public ResponseEntity<WeighBridgeTicketDto> recordWeighOut(@RequestBody @Valid final RecordWeighOutDto request,
                                                               @AuthenticationPrincipal final Jwt jwt) {
        final TruckLicensePlate truckLicensePlate = new TruckLicensePlate(request.truckLicensePlate());
        logUserActivity(LOGGER, jwt, format("is at a Weigh Bridge for the weigh-out with Truck %s",
            truckLicensePlate
        ));
        final RecordTruckWeighOutCommand command = new RecordTruckWeighOutCommand(
            truckLicensePlate,
            request.tareWeight()
        );
        final WeighBridgeTransaction weighOutTransaction = recordTruckWeighOutUseCase.recordWeighOut(command);
        final WeighBridgeTicketDto wbtDto = WeighBridgeTicketDto.fromDomain(weighOutTransaction);

        LOGGER.info("Created Weigh Bridge Ticket (WBT): {}", wbtDto);
        return ResponseEntity.status(HttpStatus.OK).body(wbtDto); // Existing WeighBridgeTransaction updated -> HttpStatus.OK
    }
}
