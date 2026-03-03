package be.kdg.prog6.landside.port.in.usecase;

import be.kdg.prog6.landside.domain.Visit;
import be.kdg.prog6.landside.port.in.command.ConsolidateTruckAtExitGateCommand;

@FunctionalInterface
public interface ConsolidateTruckAtExitGateUseCase {
    Visit consolidateTruck(ConsolidateTruckAtExitGateCommand command);
}
