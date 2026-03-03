package be.kdg.prog6.landside.adapter.in.web.controller;

import be.kdg.prog6.landside.adapter.in.web.dto.TruckOverviewDto;
import be.kdg.prog6.landside.domain.AppointmentId;
import be.kdg.prog6.landside.port.in.query.GetTruckOverviewsQuery;
import be.kdg.prog6.landside.port.in.usecase.query.GetNumberOfTrucksOnSiteUseCase;
import be.kdg.prog6.landside.port.in.usecase.query.GetTruckOverviewUseCase;
import be.kdg.prog6.landside.port.in.usecase.query.GetTruckOverviewsUseCase;
import be.kdg.prog6.landside.port.in.usecase.query.GetTrucksOnSiteUseCase;
import be.kdg.prog6.landside.port.in.usecase.query.readmodel.TruckOverview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;
import static java.lang.String.format;

@RestController
@RequestMapping("/landside/monitoring")
public class LandsideMonitoringController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LandsideMonitoringController.class);

    private final GetTrucksOnSiteUseCase getTrucksOnSiteUseCase;
    private final GetNumberOfTrucksOnSiteUseCase getNumberOfTrucksOnSiteUseCase;
    private final GetTruckOverviewsUseCase getTruckOverviewsUseCase;
    private final GetTruckOverviewUseCase getTruckOverviewUseCase;

    public LandsideMonitoringController(final GetTrucksOnSiteUseCase getTrucksOnSiteUseCase,
                                        final GetNumberOfTrucksOnSiteUseCase getNumberOfTrucksOnSiteUseCase,
                                        final GetTruckOverviewsUseCase getTruckOverviewsUseCase,
                                        final GetTruckOverviewUseCase getTruckOverviewUseCase) {
        this.getTrucksOnSiteUseCase = getTrucksOnSiteUseCase;
        this.getNumberOfTrucksOnSiteUseCase = getNumberOfTrucksOnSiteUseCase;
        this.getTruckOverviewsUseCase = getTruckOverviewsUseCase;
        this.getTruckOverviewUseCase = getTruckOverviewUseCase;
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to get overview of all trucks currently on site
     */
    @GetMapping("/trucks-on-site")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<TruckOverviewDto>> getTrucksOnSite(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is viewing Trucks currently on site");
        final List<TruckOverview> truckOverviews = getTrucksOnSiteUseCase.getTrucksOnSite();

        final List<TruckOverviewDto> truckOverviewDtos = truckOverviews
            .stream()
            .map(TruckOverviewDto::from)
            .toList();
        return ResponseEntity.ok(truckOverviewDtos);
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to know "how many" trucks are on site so that in case of emergency, I know if there is anyone on site or not.
     */
    @GetMapping("/trucks-on-site/count")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<Integer> getNumberOfTrucksOnSite(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is viewing the Number Of Trucks On Site");
        final int count = getNumberOfTrucksOnSiteUseCase.getNumberOfTrucksOnSite();
        return ResponseEntity.ok(count);
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to get all the trucks that are scheduled between given dates to have an overview of trucks.
     */
    @GetMapping("/truck-overviews")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<TruckOverviewDto>> getTruckOverviewsBetween(
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate from,
        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate to,
        @AuthenticationPrincipal final Jwt jwt
    ) {
        logUserActivity(LOGGER, jwt, format("is viewing Truck Overviews between %s and %s",
            from, to
        ));
        final GetTruckOverviewsQuery query = new GetTruckOverviewsQuery(
            from,
            to
        );
        final List<TruckOverview> truckOverviews = getTruckOverviewsUseCase.getTruckOverviews(query);

        final List<TruckOverviewDto> truckOverviewDtos = truckOverviews
            .stream()
            .map(TruckOverviewDto::from)
            .toList();
        return ResponseEntity.ok(truckOverviewDtos);
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to get overview of the truck associated with a certain appointment
     */
    @GetMapping("/truck-overview/{appointmentId}")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<TruckOverviewDto> getTruckOverview(@PathVariable("appointmentId") final UUID appointmentId,
                                                             @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is viewing Truck Overview for Appointment ID: %s",
            appointmentId
        ));
        final TruckOverview overview = getTruckOverviewUseCase.getTruckOverview(
            AppointmentId.of(appointmentId)
        );
        return ResponseEntity.ok(TruckOverviewDto.from(overview));
    }
}