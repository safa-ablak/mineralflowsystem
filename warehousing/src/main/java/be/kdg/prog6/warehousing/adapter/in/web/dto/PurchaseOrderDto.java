package be.kdg.prog6.warehousing.adapter.in.web.dto;

import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;

import java.time.LocalDateTime;
import java.util.List;

public record PurchaseOrderDto(
    String purchaseOrderId,
    String poNumber,
    String buyerId,
    String buyerName,
    String sellerId,
    String sellerName,
    LocalDateTime orderDate,
    List<OrderLineDto> orderLines,
    String status
) {
    public static PurchaseOrderDto fromDomain(final PurchaseOrder purchaseOrder) {
        return new PurchaseOrderDto(
            purchaseOrder.getPurchaseOrderId().id().toString(),
            purchaseOrder.getPoNumber().value(),
            purchaseOrder.getBuyerId().id().toString(),
            purchaseOrder.getBuyerName(),
            purchaseOrder.getSellerId().id().toString(),
            purchaseOrder.getSellerName(),
            purchaseOrder.getOrderDate(),
            purchaseOrder.getOrderLines().stream().map(OrderLineDto::fromDomain).toList(),
            purchaseOrder.getStatus().name()
        );
    }
}
