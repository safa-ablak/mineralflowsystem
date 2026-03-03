package be.kdg.prog6.invoicing.core;

import be.kdg.prog6.invoicing.domain.RawMaterial;
import be.kdg.prog6.invoicing.port.in.usecase.query.GetRawMaterialsUseCase;
import be.kdg.prog6.invoicing.port.out.LoadRawMaterialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static be.kdg.prog6.common.BoundedContext.INVOICING;
import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetRawMaterialsUseCaseImpl implements GetRawMaterialsUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetRawMaterialsUseCaseImpl.class);

    private final LoadRawMaterialPort loadRawMaterialPort;

    public GetRawMaterialsUseCaseImpl(final LoadRawMaterialPort loadRawMaterialPort) {
        this.loadRawMaterialPort = loadRawMaterialPort;
    }

    @Override
    public List<RawMaterial> getRawMaterials() {
        LOGGER.info("Getting all Raw Materials at {} ({})", KDG, INVOICING);
        final List<RawMaterial> rawMaterials = loadRawMaterialPort.loadRawMaterials();
        LOGGER.info("Found {} Raw Materials", rawMaterials.size());
        return rawMaterials;
    }
}
