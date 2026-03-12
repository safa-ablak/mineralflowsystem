package be.kdg.prog6.waterside.port.out;

import be.kdg.prog6.waterside.domain.InspectionOperation;
import be.kdg.prog6.waterside.domain.InspectionOperationStatus;

import java.util.List;

public interface InspectionOperationQueryPort {
    List<InspectionOperation> loadInspectionOperationsByStatus(InspectionOperationStatus status);
}
