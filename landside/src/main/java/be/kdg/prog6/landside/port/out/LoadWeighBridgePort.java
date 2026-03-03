package be.kdg.prog6.landside.port.out;

import be.kdg.prog6.landside.domain.VisitId;
import be.kdg.prog6.landside.domain.WeighBridge;

import java.util.List;
import java.util.Optional;

public interface LoadWeighBridgePort {
    Optional<WeighBridge> loadClosestAvailableWeighBridge();

    Optional<WeighBridge> loadByOccupiedVisitId(VisitId visitId);

    List<WeighBridge> loadWeighBridges();
}