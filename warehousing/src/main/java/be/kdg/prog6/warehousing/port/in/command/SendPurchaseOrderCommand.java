package be.kdg.prog6.warehousing.port.in.command;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.purchaseorder.OrderLine;

import java.util.List;

import static java.util.Objects.requireNonNull;

public record SendPurchaseOrderCommand(
    BuyerId buyerId,
    SellerId sellerId,
    List<OrderLine> orderLines // one order line per raw material specified
) {
    public SendPurchaseOrderCommand {
        requireNonNull(buyerId);
        requireNonNull(sellerId);
        requireNonNull(orderLines);
        if (orderLines.isEmpty()) {
            throw new InvalidOperationException("Order lines must not be empty");
        }
    }
}
