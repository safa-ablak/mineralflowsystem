package be.kdg.prog6.warehousing.core;

import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetRawMaterialSummaryUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.RawMaterialSummary;
import be.kdg.prog6.warehousing.port.out.LoadWarehousePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetRawMaterialSummaryUseCaseImpl implements GetRawMaterialSummaryUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetRawMaterialSummaryUseCaseImpl.class);

    private final LoadWarehousePort loadWarehousePort;

    public GetRawMaterialSummaryUseCaseImpl(final LoadWarehousePort loadWarehousePort) {
        this.loadWarehousePort = loadWarehousePort;
    }

    @Override
    public RawMaterialSummary getRawMaterialSummary(final RawMaterial rawMaterial) {
        final List<Warehouse> warehouses =
            loadWarehousePort.loadWarehousesByRawMaterial(rawMaterial);

        final BigDecimal totalAmount = warehouses.stream()
            .map(Warehouse::balance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        LOGGER.info("At {}, total amount of {} across all Warehouses is {} tons",
            KDG, rawMaterial.getDisplayName(), totalAmount
        );
        return new RawMaterialSummary(rawMaterial, totalAmount);
    }
}