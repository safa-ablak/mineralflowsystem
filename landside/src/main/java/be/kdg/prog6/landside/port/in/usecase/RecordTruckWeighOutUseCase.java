package be.kdg.prog6.landside.port.in.usecase;

import be.kdg.prog6.landside.domain.WeighBridgeTransaction;
import be.kdg.prog6.landside.port.in.command.RecordTruckWeighOutCommand;

@FunctionalInterface
public interface RecordTruckWeighOutUseCase {
    WeighBridgeTransaction recordWeighOut(RecordTruckWeighOutCommand command);
}
