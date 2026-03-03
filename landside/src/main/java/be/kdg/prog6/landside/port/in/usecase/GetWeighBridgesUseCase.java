package be.kdg.prog6.landside.port.in.usecase;

import be.kdg.prog6.landside.domain.WeighBridge;

import java.util.List;

@FunctionalInterface
public interface GetWeighBridgesUseCase {
    List<WeighBridge> getWeighBridges();
}
