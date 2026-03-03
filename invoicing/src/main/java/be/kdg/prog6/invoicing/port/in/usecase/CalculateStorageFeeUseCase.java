package be.kdg.prog6.invoicing.port.in.usecase;

import be.kdg.prog6.invoicing.port.in.command.CalculateStorageFeeCommand;

/*
 * Calculate storage fee each day per customer
 */
@FunctionalInterface
public interface CalculateStorageFeeUseCase {
    void calculateStorageFee(CalculateStorageFeeCommand command);
}
