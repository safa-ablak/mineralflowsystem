package be.kdg.prog6.waterside.port.in.usecase.query;

import be.kdg.prog6.waterside.domain.ShippingOrderId;
import be.kdg.prog6.waterside.port.in.usecase.query.readmodel.OperationsOverview;

@FunctionalInterface
public interface GetOperationsOverviewUseCase {
    OperationsOverview getOperationsOverview(ShippingOrderId shippingOrderId);
}
