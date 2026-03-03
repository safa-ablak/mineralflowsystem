package be.kdg.prog6.warehousing.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SendPurchaseOrderDto(
    @NotNull(message = "SellerId cannot be null")
    UUID sellerId,
    @NotEmpty(message = "Order lines cannot be empty")
    List<OrderLineRequestDto> orderLines
) {
}
