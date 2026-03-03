package be.kdg.prog6.waterside.adapter.in.web.controller;

import be.kdg.prog6.common.security.UserRole;
import be.kdg.prog6.waterside.adapter.in.web.dto.InspectionOperationDto;
import be.kdg.prog6.waterside.adapter.in.web.dto.OperationsOverviewDto;
import be.kdg.prog6.waterside.adapter.in.web.dto.ShippingOrderDto;
import be.kdg.prog6.waterside.adapter.in.web.dto.request.EnterShippingOrderDto;
import be.kdg.prog6.waterside.adapter.in.web.dto.request.InspectShipDto;
import be.kdg.prog6.waterside.adapter.in.web.dto.request.RequestShippingOrderCorrectionDto;
import be.kdg.prog6.waterside.domain.*;
import be.kdg.prog6.waterside.port.in.command.*;
import be.kdg.prog6.waterside.port.in.usecase.*;
import be.kdg.prog6.waterside.port.in.usecase.query.*;
import be.kdg.prog6.waterside.port.in.usecase.query.readmodel.OperationsOverview;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;
import static be.kdg.prog6.common.security.UserRoleUtil.extractRole;
import static java.lang.String.format;

@RestController
@RequestMapping("/shipping-orders")
public class ShippingOrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingOrderController.class);

    private final EnterShippingOrderUseCase enterShippingOrderUseCase;
    private final RequestShippingOrderCorrectionUseCase requestShippingOrderCorrectionUseCase;
    private final DockShipUseCase dockShipUseCase;
    private final GetShippingOrderUseCase getShippingOrderUseCase;
    private final GetShippingOrdersUseCase getShippingOrdersUseCase;
    private final GetShippingOrdersOnSiteUseCase getShippingOrdersOnSiteUseCase;
    private final GetNumberOfShipsOnSiteUseCase getNumberOfShipsOnSiteUseCase;
    private final GetOperationsOverviewUseCase getOperationsOverviewUseCase;
    private final GetOutstandingInspectionOperationsUseCase getOutstandingInspectionOperationsUseCase;
    private final InspectShipUseCase inspectShipUseCase;
    private final BunkerShipUseCase bunkerShipUseCase;
    private final InitiateShippingOrderLoadingUseCase initiateShippingOrderLoadingUseCase;

    public ShippingOrderController(final EnterShippingOrderUseCase enterShippingOrderUseCase,
                                   final RequestShippingOrderCorrectionUseCase requestShippingOrderCorrectionUseCase,
                                   final DockShipUseCase dockShipUseCase,
                                   final GetShippingOrderUseCase getShippingOrderUseCase,
                                   final GetShippingOrdersUseCase getShippingOrdersUseCase,
                                   final GetNumberOfShipsOnSiteUseCase getNumberOfShipsOnSiteUseCase,
                                   final GetOperationsOverviewUseCase getOperationsOverviewUseCase,
                                   final GetShippingOrdersOnSiteUseCase getShippingOrdersOnSiteUseCase,
                                   final GetOutstandingInspectionOperationsUseCase getOutstandingInspectionOperationsUseCase,
                                   final InspectShipUseCase inspectShipUseCase,
                                   final BunkerShipUseCase bunkerShipUseCase,
                                   final InitiateShippingOrderLoadingUseCase initiateShippingOrderLoadingUseCase) {
        this.enterShippingOrderUseCase = enterShippingOrderUseCase;
        this.requestShippingOrderCorrectionUseCase = requestShippingOrderCorrectionUseCase;
        this.dockShipUseCase = dockShipUseCase;
        this.getShippingOrderUseCase = getShippingOrderUseCase;
        this.getShippingOrdersUseCase = getShippingOrdersUseCase;
        this.getNumberOfShipsOnSiteUseCase = getNumberOfShipsOnSiteUseCase;
        this.getOperationsOverviewUseCase = getOperationsOverviewUseCase;
        this.getShippingOrdersOnSiteUseCase = getShippingOrdersOnSiteUseCase;
        this.getOutstandingInspectionOperationsUseCase = getOutstandingInspectionOperationsUseCase;
        this.inspectShipUseCase = inspectShipUseCase;
        this.bunkerShipUseCase = bunkerShipUseCase;
        this.initiateShippingOrderLoadingUseCase = initiateShippingOrderLoadingUseCase;
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>ship captain</b>, I want to input the Shipping Order and vessel information upon arrival at the loading quay,
     * so that the necessary operations, inspection, bunkering and loading can be planned and initiated.<br></br>
     * <p>
     * Buyer ID is included to ensure one provided for the entered Shipping Order (SO) matches to that of the referenced Purchase Order (PO).
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SHIP_CAPTAIN', 'ROLE_ADMIN')")
    public ResponseEntity<ShippingOrderDto> enterShippingOrder(@RequestBody @Valid final EnterShippingOrderDto request,
                                                               @AuthenticationPrincipal final Jwt jwt) {
        final BuyerId buyerId = BuyerId.of(request.buyerId());
        logUserActivity(LOGGER, jwt, format("is entering a Shipping Order for Buyer with ID %s",
            buyerId.id()
        ));
        final EnterShippingOrderCommand command = new EnterShippingOrderCommand(
            buyerId,
            ReferenceId.of(request.referenceId()), // PO Reference
            new VesselNumber(request.vesselNumber()),
            request.scheduledArrivalDate(),
            request.scheduledDepartureDate()
        );
        final ShippingOrder shippingOrderToBeValidated = enterShippingOrderUseCase.enterShippingOrder(command);
        // ACCEPTED(202) instead of CREATED(201) because processing continues asynchronously via events
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(ShippingOrderDto.fromDomain(shippingOrderToBeValidated));
    }

    /**
     * 📘 - User Story<br>
     * As a <b>ship captain</b>, I want to request a correction for a Shipping Order, if Buyer
     * information or PO Reference were not entered correctly, so that it can be properly matched
     * with a Purchase Order and support operations like inspection, bunkering and loading.
     */
    @PostMapping("/{id}/correction-requests")
    @PreAuthorize("hasAnyRole('ROLE_SHIP_CAPTAIN', 'ROLE_ADMIN')")
    public ResponseEntity<ShippingOrderDto> requestShippingOrderCorrection(
        @PathVariable final UUID id,
        @RequestBody @Valid final RequestShippingOrderCorrectionDto request,
        @AuthenticationPrincipal final Jwt jwt
    ) {
        final ShippingOrderId shippingOrderId = ShippingOrderId.of(id);
        logUserActivity(LOGGER, jwt, format("is requesting a correction for Shipping Order with ID %s",
            shippingOrderId.id()
        ));
        final RequestShippingOrderCorrectionCommand command = new RequestShippingOrderCorrectionCommand(
            shippingOrderId,
            BuyerId.ofNullable(request.buyerId()),
            ReferenceId.ofNullable(request.referenceId())
        );
        final ShippingOrder shippingOrderToBeCorrected =
            requestShippingOrderCorrectionUseCase.requestShippingOrderCorrection(command);
        // ACCEPTED(202) instead of OK(200) because processing continues asynchronously via events
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(ShippingOrderDto.fromDomain(shippingOrderToBeCorrected));
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>ship captain</b>, I want to dock my ship at the port.
     */
    @PostMapping("/{id}/dock")
    @PreAuthorize("hasAnyRole('ROLE_SHIP_CAPTAIN', 'ROLE_ADMIN')")
    public ResponseEntity<ShippingOrderDto> dockShip(@PathVariable final UUID id,
                                                     @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is docking the ship associated with Shipping Order ID %s",
            id
        ));
        final DockShipCommand command = new DockShipCommand(
            ShippingOrderId.of(id)
        );
        final ShippingOrder shippingOrder = dockShipUseCase.dockShip(command);

        return ResponseEntity.ok(ShippingOrderDto.fromDomain(shippingOrder));
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>foreman</b>, I want to view the full details of a Shipping Order.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_FOREMAN', 'ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<ShippingOrderDto> getShippingOrder(@PathVariable final UUID id,
                                                             @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is viewing details of the Shipping Order with ID %s",
            id
        ));
        final ShippingOrder shippingOrder = getShippingOrderUseCase.getShippingOrder(
            ShippingOrderId.of(id)
        );
        return ResponseEntity.ok(ShippingOrderDto.fromDomain(shippingOrder));
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>foreman</b>, I want to see all shipping orders containing relevant information
     * such as vessel number, inspection status and so on.<br></br>
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_FOREMAN', 'ROLE_ADMIN')")
    public ResponseEntity<List<ShippingOrderDto>> getShippingOrders(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is viewing All Shipping Orders");
        final List<ShippingOrder> shippingOrders = getShippingOrdersUseCase.getShippingOrders();

        final List<ShippingOrderDto> shippingOrderDtos = shippingOrders
            .stream()
            .map(ShippingOrderDto::fromDomain)
            .toList();
        return ResponseEntity.ok(shippingOrderDtos);
    }

    @GetMapping("/on-site")
    @PreAuthorize("hasAnyRole('ROLE_FOREMAN', 'ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ShippingOrderDto>> getShippingOrdersOnSite(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is viewing All Shipping Orders On Site");
        final List<ShippingOrder> shippingOrdersOnSite = getShippingOrdersOnSiteUseCase.getShippingOrdersOnSite();

        final List<ShippingOrderDto> shippingOrderDtos = shippingOrdersOnSite
            .stream()
            .map(ShippingOrderDto::fromDomain)
            .toList();
        return ResponseEntity.ok(shippingOrderDtos);
    }

    @GetMapping("on-site/count")
    @PreAuthorize("hasAnyRole('ROLE_FOREMAN', 'ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<Integer> getNumberOfShipsOnSite(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is viewing the Number Of Ships On Site");
        final int numberOfShipsOnSite = getNumberOfShipsOnSiteUseCase.getNumberOfShipsOnSite();
        return ResponseEntity.ok(numberOfShipsOnSite);
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>ship captain</b>, I want an overview of all operations (IO, BO) to know if I can leave the port.
     */
    @GetMapping("/{id}/operations")
    @PreAuthorize("hasAnyRole('ROLE_SHIP_CAPTAIN', 'ROLE_ADMIN')")
    public ResponseEntity<OperationsOverviewDto> getOperationsOverview(@PathVariable final UUID id,
                                                                       @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is viewing Operations Overview for Shipping Order with ID %s",
            id
        ));
        final OperationsOverview overview = getOperationsOverviewUseCase.getOperationsOverview(
            ShippingOrderId.of(id)
        );
        return ResponseEntity.ok(OperationsOverviewDto.from(overview));
    }

    // --- Inspection Operations ---

    /**
     * 📘 - User Story<br></br>
     * As an <b>inspector</b>, I want to see all outstanding IOs.
     */
    @GetMapping("/inspection-operations/outstanding")
    @PreAuthorize("hasAnyRole('ROLE_INSPECTOR', 'ROLE_ADMIN')")
    public ResponseEntity<List<InspectionOperationDto>> getOutstandingInspectionOperations(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is viewing Outstanding Inspection Operations");
        final List<InspectionOperation> outstandingInspectionOperations =
            getOutstandingInspectionOperationsUseCase.getOutstandingInspectionOperations();

        final List<InspectionOperationDto> outstandingInspectionOperationDtos = outstandingInspectionOperations
            .stream()
            .map(InspectionOperationDto::fromDomain)
            .toList();
        return ResponseEntity.ok(outstandingInspectionOperationDtos);
    }

    /**
     * 📘 - User Story<br></br>
     * As an <b>inspector</b>, I want to complete inspections so that vessels can be inspected.<br></br>
     * Besides this, a warehouse manager can look up a shipment order and mark the IO complete.
     */
    @PostMapping("/{id}/inspect")
    @PreAuthorize("hasAnyRole('ROLE_INSPECTOR', 'ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<ShippingOrderDto> inspectShip(@PathVariable final UUID id,
                                                        @RequestBody @Valid final InspectShipDto request,
                                                        @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt,
            format("is performing the Inspection Operation for the Ship associated with Shipping Order ID %s",
                id
            ));
        final InspectShipCommand command = new InspectShipCommand(
            ShippingOrderId.of(id),
            request.inspectorSignature()
        );
        final ShippingOrder shippingOrder = inspectShipUseCase.inspectShip(command);

        return ResponseEntity.ok(ShippingOrderDto.fromDomain(shippingOrder));
    }

    // --- Bunkering Operations ---

    /**
     * 📘 - User Story<br></br>
     * As a <b>bunkering officer</b>, I want to plan all BOs with a maximum of 6 a day so that vessels can be refueled.<br></br>
     * Besides this, a <b>warehouse manager</b> can look up a shipment order and mark the BO complete.<br></br>
     * Note: Daily limit of 6 BOs only applies to the <b>bunkering officer</b> role.
     */
    @PostMapping("/{id}/bunker")
    @PreAuthorize("hasAnyRole('ROLE_BUNKERING_OFFICER', 'ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<ShippingOrderDto> bunkerShip(@PathVariable final UUID id,
                                                       @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt,
            format("is performing the Bunkering Operation for the Ship associated with Shipping Order ID %s",
                id
            ));
        final UserRole userRole = extractRole(jwt.getClaims());
        final BunkerShipCommand command = new BunkerShipCommand(
            ShippingOrderId.of(id),
            userRole
        );
        final ShippingOrder shippingOrder = bunkerShipUseCase.bunkerShip(command);

        return ResponseEntity.ok(ShippingOrderDto.fromDomain(shippingOrder));
    }

    // --- Loading ---

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want the oldest stock of raw materials for loading allocated automatically
     * so that the seller is charged as little as possible for storage.
     */
    @PostMapping("/{id}/load")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<ShippingOrderDto> loadShippingOrderToShip(@PathVariable final UUID id,
                                                                    @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt,
            format("is initiating the loading of Raw Materials onto the Ship associated with Shipping Order ID %s",
                id
            ));
        final InitiateShippingOrderLoadingCommand command = new InitiateShippingOrderLoadingCommand(
            ShippingOrderId.of(id)
        );
        final ShippingOrder shippingOrderToBeLoaded =
            initiateShippingOrderLoadingUseCase.initiateShippingOrderLoading(command);
        // ACCEPTED(202) instead of OK(200) because processing continues asynchronously via events
        return ResponseEntity
            .status(HttpStatus.ACCEPTED)
            .body(ShippingOrderDto.fromDomain(shippingOrderToBeLoaded));
    }
}