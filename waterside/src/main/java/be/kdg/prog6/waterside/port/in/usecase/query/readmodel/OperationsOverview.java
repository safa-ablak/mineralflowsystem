package be.kdg.prog6.waterside.port.in.usecase.query.readmodel;

import be.kdg.prog6.waterside.domain.BunkeringOperation;
import be.kdg.prog6.waterside.domain.InspectionOperation;
import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.domain.ShippingOrderId;

public record OperationsOverview(
    ShippingOrderId shippingOrderId,
    InspectionOperation inspection,
    BunkeringOperation bunkering,
    boolean shipReadyForLoading
) {
    public static OperationsOverview fromDomain(final ShippingOrder shippingOrder) {
        return new OperationsOverview(
            shippingOrder.getShippingOrderId(),
            shippingOrder.getInspection(),
            shippingOrder.getBunkering(),
            shippingOrder.isReadyForLoading()
        );
    }
}
