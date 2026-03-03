package be.kdg.prog6.landside.port.in.usecase;

import be.kdg.prog6.landside.port.in.command.RecognizeTruckAtEntranceGateCommand;

@FunctionalInterface
public interface RecognizeTruckAtEntranceGateUseCase {
    String recognizeTruckAndAssignWeighBridge(RecognizeTruckAtEntranceGateCommand command);
}
