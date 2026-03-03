package be.kdg.prog6.waterside.port.in.usecase.query;

import be.kdg.prog6.waterside.domain.InspectionOperation;

import java.util.List;

@FunctionalInterface
public interface GetOutstandingInspectionOperationsUseCase {
    List<InspectionOperation> getOutstandingInspectionOperations();
}
