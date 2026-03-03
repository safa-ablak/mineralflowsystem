package be.kdg.prog6.landside.adapter.in.web.controller;

import be.kdg.prog6.landside.adapter.in.web.dto.DockedTruckDto;
import be.kdg.prog6.landside.adapter.in.web.dto.request.DockTruckDto;
import be.kdg.prog6.landside.domain.TruckLicensePlate;
import be.kdg.prog6.landside.port.in.command.DockTruckCommand;
import be.kdg.prog6.landside.port.in.usecase.DockTruckUseCase;
import be.kdg.prog6.landside.port.in.usecase.model.DockedTruck;
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
@RequestMapping("/landside/warehouses")
public class LandsideWarehouseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LandsideWarehouseController.class);

    private final DockTruckUseCase dockTruckUseCase;

    public LandsideWarehouseController(final DockTruckUseCase dockTruckUseCase) {
        this.dockTruckUseCase = dockTruckUseCase;
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>truck driver</b>, I want to dock to the correct conveyor belt
     * and receive my copy of the Payload Delivery Ticket (PDT) and new weighing bridge number.
     */
    @PostMapping("/dock")
    @PreAuthorize("hasAnyRole('ROLE_TRUCK_DRIVER', 'ROLE_ADMIN')")
    public ResponseEntity<DockedTruckDto> dockTruck(@RequestBody @Valid final DockTruckDto request,
                                                    @AuthenticationPrincipal final Jwt jwt) {
        final String truckLicensePlate = request.truckLicensePlate();
        logUserActivity(LOGGER, jwt, format("is docking Truck %s at a Conveyor Belt",
            truckLicensePlate
        ));
        final DockTruckCommand command = new DockTruckCommand(
            new TruckLicensePlate(truckLicensePlate)
        );
        final DockedTruck dockedTruck = dockTruckUseCase.dockTruck(command);

        return ResponseEntity.ok(DockedTruckDto.from(dockedTruck));
    }
}
