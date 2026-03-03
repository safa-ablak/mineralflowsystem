package be.kdg.prog6.warehousing.port.in.usecase.query;

import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.RawMaterialSummary;

@FunctionalInterface
public interface GetRawMaterialSummaryUseCase {
    RawMaterialSummary getRawMaterialSummary(RawMaterial rawMaterial);
}
