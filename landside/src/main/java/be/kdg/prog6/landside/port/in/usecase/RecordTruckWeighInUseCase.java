package be.kdg.prog6.landside.port.in.usecase;

import be.kdg.prog6.landside.domain.WeighBridgeTransaction;
import be.kdg.prog6.landside.port.in.command.RecordTruckWeighInCommand;

@FunctionalInterface
public interface RecordTruckWeighInUseCase {
    WeighBridgeTransaction recordWeighIn(RecordTruckWeighInCommand command);
}
