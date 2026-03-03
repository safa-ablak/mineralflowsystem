package be.kdg.prog6.waterside.port.in.usecase;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.in.command.InitiateShippingOrderLoadingCommand;

@FunctionalInterface
public interface InitiateShippingOrderLoadingUseCase {
    ShippingOrder initiateShippingOrderLoading(InitiateShippingOrderLoadingCommand command);
}
