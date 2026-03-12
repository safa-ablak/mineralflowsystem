package be.kdg.prog6.warehousing.adapter.in.web.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SendPurchaseOrderDto(
    @Nullable // If Admin is sending a PO on behalf of a Buyer; won't be provided by Buyers as it is taken from JWT
    UUID buyerId,
    @NotNull(message = "SellerId cannot be null")
    UUID sellerId,
    @NotEmpty(message = "Order lines cannot be empty")
    List<OrderLineRequestDto> orderLines
) {}
