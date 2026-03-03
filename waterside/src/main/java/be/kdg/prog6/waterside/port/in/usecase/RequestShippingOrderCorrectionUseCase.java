package be.kdg.prog6.waterside.port.in.usecase;

import be.kdg.prog6.waterside.domain.ShippingOrder;
import be.kdg.prog6.waterside.port.in.command.RequestShippingOrderCorrectionCommand;

/*
 * If the Shipping Order is entered incorrectly
 * (e.g. Incorrect Buyer ID or invalid Reference ID (PO Reference))
 * then we should be able to correct that Shipping Order via its ID
 * */
@FunctionalInterface
public interface RequestShippingOrderCorrectionUseCase {
    ShippingOrder requestShippingOrderCorrection(RequestShippingOrderCorrectionCommand command);
}
