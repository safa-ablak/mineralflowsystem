package be.kdg.prog6.warehousing.adapter.in.web.controller;

import be.kdg.prog6.warehousing.adapter.in.web.dto.PurchaseOrderDto;
import be.kdg.prog6.warehousing.adapter.in.web.dto.request.SendPurchaseOrderDto;
import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.purchaseorder.OrderLine;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderStatus;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.port.in.command.SendPurchaseOrderCommand;
import be.kdg.prog6.warehousing.port.in.query.GetPurchaseOrdersQuery;
import be.kdg.prog6.warehousing.port.in.usecase.SendPurchaseOrderUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetPurchaseOrderUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetPurchaseOrdersUseCase;
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
import static java.lang.String.format;

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderController.class);

    private final SendPurchaseOrderUseCase sendPurchaseOrderUseCase;
    private final GetPurchaseOrderUseCase getPurchaseOrderUseCase;
    private final GetPurchaseOrdersUseCase getPurchaseOrdersUseCase;

    public PurchaseOrderController(final SendPurchaseOrderUseCase sendPurchaseOrderUseCase,
                                   final GetPurchaseOrderUseCase getPurchaseOrderUseCase,
                                   final GetPurchaseOrdersUseCase getPurchaseOrdersUseCase) {
        this.sendPurchaseOrderUseCase = sendPurchaseOrderUseCase;
        this.getPurchaseOrderUseCase = getPurchaseOrderUseCase;
        this.getPurchaseOrdersUseCase = getPurchaseOrdersUseCase;
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>buyer</b>, I want to send a PO to KdG to make sure they can expect a shipment soon.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_BUYER', 'ROLE_ADMIN')")
    public ResponseEntity<PurchaseOrderDto> sendPurchaseOrder(@RequestBody @Valid final SendPurchaseOrderDto request,
                                                              @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is sending a Purchase Order");
        final List<OrderLine> orderLines = request.orderLines()
            .stream()
            .map(orderLineRequest -> new OrderLine(
                RawMaterial.fromString(orderLineRequest.rawMaterial()),
                orderLineRequest.amount()
            ))
            .toList();

        final SendPurchaseOrderCommand command = new SendPurchaseOrderCommand(
            BuyerId.of(UUID.fromString(jwt.getSubject())),
            SellerId.of(request.sellerId()),
            orderLines
        );
        final PurchaseOrder sentPurchaseOrder = sendPurchaseOrderUseCase.sendPurchaseOrder(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(PurchaseOrderDto.fromDomain(sentPurchaseOrder));
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to view a specific PO.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<PurchaseOrderDto> getPurchaseOrder(@PathVariable("id") final UUID id,
                                                             @AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, format("is viewing Purchase Order with ID %s",
            id
        ));
        final PurchaseOrder purchaseOrder = getPurchaseOrderUseCase.getPurchaseOrder(PurchaseOrderId.of(id));

        return ResponseEntity.ok(PurchaseOrderDto.fromDomain(purchaseOrder));
    }

    /**
     * 📘 - User Story<br></br>
     * As a <b>warehouse manager</b>, I want to view purchase orders filtered by their fulfillment status and/or by the seller,
     * so I can easily find the relevant orders.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_WAREHOUSE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<PurchaseOrderDto>> getPurchaseOrders(
        @RequestParam(required = false) final PurchaseOrderStatus status,
        @RequestParam(required = false) final String sellerName,
        @AuthenticationPrincipal final Jwt jwt
    ) {
        logUserActivity(LOGGER, jwt, format("is viewing Purchase Orders (Status: %s, Seller Name: %s)",
            status, sellerName
        ));
        final GetPurchaseOrdersQuery query = new GetPurchaseOrdersQuery(
            status,
            sellerName
        );
        final List<PurchaseOrder> purchaseOrders = getPurchaseOrdersUseCase.getPurchaseOrders(
            query
        );
        final List<PurchaseOrderDto> purchaseOrderDtos = purchaseOrders
            .stream()
            .map(PurchaseOrderDto::fromDomain)
            .toList();
        return ResponseEntity.ok(purchaseOrderDtos);
    }
}
