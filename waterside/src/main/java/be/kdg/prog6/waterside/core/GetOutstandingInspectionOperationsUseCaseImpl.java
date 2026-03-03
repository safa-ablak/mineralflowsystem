package be.kdg.prog6.waterside.core;

import be.kdg.prog6.waterside.domain.InspectionOperation;
import be.kdg.prog6.waterside.domain.InspectionOperationStatus;
import be.kdg.prog6.waterside.port.in.usecase.query.GetOutstandingInspectionOperationsUseCase;
import be.kdg.prog6.waterside.port.out.LoadInspectionOperationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Service
public class GetOutstandingInspectionOperationsUseCaseImpl implements GetOutstandingInspectionOperationsUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetOutstandingInspectionOperationsUseCaseImpl.class);

    private final LoadInspectionOperationPort loadInspectionOperationPort;

    public GetOutstandingInspectionOperationsUseCaseImpl(final LoadInspectionOperationPort loadInspectionOperationPort) {
        this.loadInspectionOperationPort = loadInspectionOperationPort;
    }

    @Override
    public List<InspectionOperation> getOutstandingInspectionOperations() {
        LOGGER.info("Getting Outstanding Inspection Operations at {}", KDG);
        final List<InspectionOperation> outstandingInspectionOperations =
            loadInspectionOperationPort.loadInspectionOperationsByStatus(InspectionOperationStatus.SCHEDULED);
        LOGGER.info("Found {} Outstanding Inspection Operations", outstandingInspectionOperations.size());
        return outstandingInspectionOperations;
    }
}
