package be.kdg.prog6.landside.adapter.out.db.adapter;

import be.kdg.prog6.landside.adapter.out.db.entity.WeighBridgeJpaEntity;
import be.kdg.prog6.landside.adapter.out.db.repository.WeighBridgeJpaRepository;
import be.kdg.prog6.landside.domain.VisitId;
import be.kdg.prog6.landside.domain.WeighBridge;
import be.kdg.prog6.landside.domain.WeighBridgeId;
import be.kdg.prog6.landside.domain.WeighBridgeNumber;
import be.kdg.prog6.landside.port.out.LoadWeighBridgePort;
import be.kdg.prog6.landside.port.out.UpdateWeighBridgePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WeighBridgeDatabaseAdapter implements LoadWeighBridgePort, UpdateWeighBridgePort {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeighBridgeDatabaseAdapter.class);

    private final WeighBridgeJpaRepository weighBridgeJpaRepository;

    public WeighBridgeDatabaseAdapter(final WeighBridgeJpaRepository weighBridgeJpaRepository) {
        this.weighBridgeJpaRepository = weighBridgeJpaRepository;
    }

    @Override
    public List<WeighBridge> loadWeighBridges() {
        LOGGER.info("Loading all Weigh Bridges");
        return weighBridgeJpaRepository.findAll().stream().map(this::toWeighBridge).toList();
    }

    /*
    * (Advanced Option): This could also be done in a way that it returns the closest available weigh bridge
    * on truck's route to the target warehouse, but for simplicity we are just returning
    * the first available weigh bridge.
    * */
    @Override
    public Optional<WeighBridge> loadClosestAvailableWeighBridge() {
        LOGGER.debug("Finding an available WeighBridge");
        return weighBridgeJpaRepository.findFirstAvailable().map(this::toWeighBridge);
    }

    @Override
    public Optional<WeighBridge> loadByOccupiedVisitId(final VisitId visitId) {
        LOGGER.debug("Finding WeighBridge occupied by Visit: {}", visitId.id());
        return weighBridgeJpaRepository.findByOccupiedByVisitId(visitId.id()).map(this::toWeighBridge);
    }

    @Override
    public void updateWeighBridge(final WeighBridge weighBridge) {
        LOGGER.info("Updating WeighBridge {}", weighBridge.getId().id());
        final WeighBridgeJpaEntity entity = toWeighBridgeJpaEntity(weighBridge);
        weighBridgeJpaRepository.save(entity);
    }

    private WeighBridge toWeighBridge(final WeighBridgeJpaEntity entity) {
        final VisitId occupiedByVisitId = entity.getOccupiedByVisitId() != null
            ? VisitId.of(entity.getOccupiedByVisitId())
            : null;
        return new WeighBridge(
            WeighBridgeId.of(entity.getWeighBridgeId()),
            WeighBridgeNumber.of(entity.getNumber()),
            occupiedByVisitId
        );
    }

    private WeighBridgeJpaEntity toWeighBridgeJpaEntity(final WeighBridge weighBridge) {
        final WeighBridgeJpaEntity entity = new WeighBridgeJpaEntity();
        entity.setWeighBridgeId(weighBridge.getId().id());
        entity.setNumber(weighBridge.getNumber().value());
        entity.setOccupiedByVisitId(weighBridge.getOccupiedByVisitId() != null
            ? weighBridge.getOccupiedByVisitId().id()
            : null
        );
        return entity;
    }
}
