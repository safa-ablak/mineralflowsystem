package be.kdg.prog6.landside.core;

import be.kdg.prog6.landside.domain.WeighBridge;
import be.kdg.prog6.landside.port.in.usecase.GetWeighBridgesUseCase;
import be.kdg.prog6.landside.port.out.LoadWeighBridgePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetWeighBridgesUseCaseImpl implements GetWeighBridgesUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetWeighBridgesUseCaseImpl.class);

    private final LoadWeighBridgePort loadWeighBridgePort;

    public GetWeighBridgesUseCaseImpl(final LoadWeighBridgePort loadWeighBridgePort) {
        this.loadWeighBridgePort = loadWeighBridgePort;
    }

    @Override
    public List<WeighBridge> getWeighBridges() {
        LOGGER.info("Getting all Weigh Bridges at {}", KDG);
        final List<WeighBridge> weighBridges = loadWeighBridgePort.loadWeighBridges();
        LOGGER.info("Found {} Weigh Bridges", weighBridges.size());
        return weighBridges;
    }
}
