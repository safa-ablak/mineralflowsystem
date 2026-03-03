package be.kdg.prog6.invoicing.port.in.usecase;

import be.kdg.prog6.invoicing.port.in.command.CalculateCommissionFeeCommand;

/*
 * Calculate commission fee on each fulfilled PO
 */
@FunctionalInterface
public interface CalculateCommissionFeeUseCase {
    void calculateCommissionFee(CalculateCommissionFeeCommand command);
}
