package be.kdg.prog6.warehousing.adapter.in.web.controller;

import be.kdg.prog6.warehousing.adapter.in.web.dto.*;
import be.kdg.prog6.warehousing.domain.storage.Balance;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.query.CalculateWarehouseBalanceChangeQuery;
import be.kdg.prog6.warehousing.port.in.query.GetWarehouseActivityHistoryQuery;
import be.kdg.prog6.warehousing.port.in.query.GetWarehouseBalanceQuery;
import be.kdg.prog6.warehousing.port.in.usecase.query.*;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;
import static java.lang.String.format;

@RestController
@RequestMapping("/warehousing/warehouses")
public class WarehousingWarehouseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehousingWarehouseController.class);

    private final GetWarehouseOverviewUseCase getWarehouseOverviewUseCase;
    private final GetWarehouseActivityHistoryUseCase getWarehouseActivityHistoryUseCase;
    private final GetWarehouseBalanceUseCase getWarehouseBalanceUseCase;
    private final CalculateWarehouseBalanceChangeUseCase calculateWarehouseBalanceChangeUseCase;
    private final GetWarehouseBalanceSnapshotHistoryUseCase getWarehouseBalanceSnapshotHistoryUseCase;
    private final GetWarehouseOverviewsUseCase getWarehouseOverviewsUseCase;
    private final GetRawMaterialSummaryUseCase getRawMaterialSummaryUseCase;

    public WarehousingWarehouseController(final GetWarehouseOverviewUseCase getWarehouseOverviewUseCase,
                                          final GetWarehouseActivityHistoryUseCase getWarehouseActivityHistoryUseCase,
                                          final GetWarehouseBalanceUseCase getWarehouseBalanceUseCase,
                                          final CalculateWarehouseBalanceChangeUseCase calculateWarehouseBalanceChangeUseCase,
                                          final GetWarehouseBalanceSnapshotHistoryUseCase getWarehouseBalanceSnapshotHistoryUseCase,
                                          final GetWarehouseOverviewsUseCase getWarehouseOverviewsUseCase,
                                          final GetRawMaterialSummaryUseCase getRawMaterialSummaryUseCase) {
        this.getWarehouseOverviewUseCase = getWarehouseOverviewUseCase;
        this.getWarehouseActivityHistoryUseCase = getWarehouseActivityHistoryUseCase;
        this.getWarehouseBalanceUseCase = getWarehouseBalanceUseCase;
        this.calculateWarehouseBalanceChangeUseCase = calculateWarehouseBalanceChangeUseCase;
        this.getWarehouseBalanceSnapshotHistoryUseCase = getWarehouseBalanceSnapshotHistoryUseCase;
        this.getWarehouseOverviewsUseCase = getWarehouseOverviewsUseCase;
        this.getRawMaterialSummaryUseCase = getRawMaterialSummaryUseCase;
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to have an overview of a specific warehouse.
     */
    @GetMapping("/{id}/overview")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<WarehouseOverviewDto> getWarehouseOverview(@PathVariable final UUID id,
                                                                     @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is viewing the Overview of Warehouse with ID %s",
            id
        ));
        final WarehouseOverview warehouseOverview = getWarehouseOverviewUseCase.getWarehouseOverview(
            WarehouseId.of(id)
        );
        return ResponseEntity.ok(WarehouseOverviewDto.from(warehouseOverview));
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to see the activity history (all deliveries and shipments) of a specific warehouse,
     * sorted from newest to oldest, so I can trace operational events and investigate stock changes.
     * <p>
     * Use {@code view-mode=WITH_ALLOCATIONS} to include FIFO allocation tracking for Delivery <-> Shipment traceability.
     */
    @GetMapping("/{id}/activity-history")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<WarehouseActivityHistoryDto> getWarehouseActivityHistory(
        @PathVariable final UUID id,
        @RequestParam(name = "view-mode", defaultValue = "WITHOUT_ALLOCATIONS")
        final GetWarehouseActivityHistoryQuery.ViewMode viewMode,
        @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is viewing the Activity History of Warehouse with ID %s",
            id
        ));
        final GetWarehouseActivityHistoryQuery query = new GetWarehouseActivityHistoryQuery(
            WarehouseId.of(id),
            viewMode
        );
        final WarehouseActivityHistory warehouseActivityHistory =
            getWarehouseActivityHistoryUseCase.getWarehouseActivityHistory(query);
        return ResponseEntity.ok(WarehouseActivityHistoryDto.from(warehouseActivityHistory));
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to query the Balance of a specific warehouse
     * (optionally as of a given point in time), so I can monitor stock levels currently or historically.
     */
    @GetMapping("/{id}/balance")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<BalanceDto> getWarehouseBalance(
        @PathVariable("id") final UUID id,
        @RequestParam(name = "as-of", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime asOf,
        @AuthenticationPrincipal final Jwt jwt
    ) {
        final LocalDateTime effectiveAsOf = asOf != null ? asOf : LocalDateTime.now();
        logUserActivity(LOGGER, jwt, format("is querying the Balance of Warehouse with ID %s as of %s",
            id, effectiveAsOf
        ));
        final GetWarehouseBalanceQuery query = new GetWarehouseBalanceQuery(
            WarehouseId.of(id),
            effectiveAsOf
        );
        final Balance balance = getWarehouseBalanceUseCase.getBalance(query);
        return ResponseEntity.ok(BalanceDto.of(balance));
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to query the net Balance change of a specific warehouse between two dates,
     * so I can track how stock levels have increased or decreased during that period.
     * <p>
     * The net change is calculated by summing all deliveries and subtracting all shipments
     * between {@code from} and {@code to} and projecting on top of an initial balance of 0 (as of {@code LocalDateTime.MIN}).
     */
    @GetMapping("/{id}/balance/net-change")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<NetBalanceChangeDto> getNetWarehouseBalanceChangeBetween(
        @PathVariable("id") final UUID id,
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime from,
        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime to,
        @AuthenticationPrincipal final Jwt jwt
    ) {
        logUserActivity(LOGGER, jwt, format("is querying the net Balance change of Warehouse with ID %s from %s to %s",
            id, from, to
        ));
        final CalculateWarehouseBalanceChangeQuery query = new CalculateWarehouseBalanceChangeQuery(
            WarehouseId.of(id),
            from,
            to
        );
        final NetBalanceChange netBalanceChange = calculateWarehouseBalanceChangeUseCase.calculateNetBalanceChange(
            query
        );
        return ResponseEntity.ok(NetBalanceChangeDto.of(netBalanceChange));
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to analyze how stock levels changed over time,
     * using recorded Balance Snapshots of a specific warehouse.
     */
    @GetMapping("/{id}/balance/snapshots")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<BalanceSnapshotDto>> getBalanceSnapshotHistory(
        @PathVariable("id") final UUID id,
        @AuthenticationPrincipal final Jwt jwt
    ) {
        logUserActivity(LOGGER, jwt, format("is viewing Balance Snapshots for Warehouse with ID %s",
            id
        ));
        final List<BalanceSnapshot> balanceSnapshots =
            getWarehouseBalanceSnapshotHistoryUseCase.getBalanceSnapshotHistoryFor(WarehouseId.of(id));
        final List<BalanceSnapshotDto> balanceSnapshotDtos = balanceSnapshots
            .stream()
            .map(BalanceSnapshotDto::of)
            .toList();
        return ResponseEntity.ok(balanceSnapshotDtos);
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to have an overview and know what the total raw material is in my warehouses
     * as well as the details of each warehouse.
     */
    @GetMapping("/overview")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<WarehouseOverviewDto>> getWarehouseOverviews(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is viewing the Overview of all Warehouses");
        final List<WarehouseOverview> warehouseOverviews = getWarehouseOverviewsUseCase.getWarehouseOverviews();

        final List<WarehouseOverviewDto> warehouseOverviewDtos = warehouseOverviews
            .stream()
            .map(WarehouseOverviewDto::from)
            .toList();
        return ResponseEntity.ok(warehouseOverviewDtos);
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to see a summary of all raw materials on-site by type.
     */
    @GetMapping("/raw-materials/{type}/summary")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<RawMaterialSummaryDto> getRawMaterialSummary(@PathVariable final String type,
                                                                       @AuthenticationPrincipal final Jwt jwt) {
        final RawMaterial rawMaterial = RawMaterial.fromString(type);
        logUserActivity(LOGGER, jwt, format("is viewing Raw Material summary for %s",
            rawMaterial.getDisplayName()
        ));
        final RawMaterialSummary summary = getRawMaterialSummaryUseCase.getRawMaterialSummary(
            rawMaterial
        );
        return ResponseEntity.ok(RawMaterialSummaryDto.of(summary));
    }
}