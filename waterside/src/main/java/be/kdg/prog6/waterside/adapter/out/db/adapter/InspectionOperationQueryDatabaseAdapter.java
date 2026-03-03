package be.kdg.prog6.waterside.adapter.out.db.adapter;

import be.kdg.prog6.waterside.adapter.out.db.entity.InspectionOperationJpaEntity;
import be.kdg.prog6.waterside.adapter.out.db.repository.InspectionOperationJpaRepository;
import be.kdg.prog6.waterside.domain.InspectionOperation;
import be.kdg.prog6.waterside.domain.InspectionOperationId;
import be.kdg.prog6.waterside.domain.InspectionOperationStatus;
import be.kdg.prog6.waterside.port.out.LoadInspectionOperationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InspectionOperationQueryDatabaseAdapter implements LoadInspectionOperationPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(InspectionOperationQueryDatabaseAdapter.class);

    private final InspectionOperationJpaRepository inspectionOperationJpaRepository;

    public InspectionOperationQueryDatabaseAdapter(final InspectionOperationJpaRepository inspectionOperationJpaRepository) {
        this.inspectionOperationJpaRepository = inspectionOperationJpaRepository;
    }

    @Override
    public List<InspectionOperation> loadInspectionOperationsByStatus(final InspectionOperationStatus status) {
        LOGGER.info("Loading Inspection Operations by Status: {}", status);
        return inspectionOperationJpaRepository.findByStatus(status)
            .stream()
            .map(this::toInspectionOperation)
            .toList();
    }

    private InspectionOperation toInspectionOperation(final InspectionOperationJpaEntity inspectionJpa) {
        return new InspectionOperation(
            InspectionOperationId.of(inspectionJpa.getId()),
            inspectionJpa.getPerformedOn(),
            inspectionJpa.getInspectorSignature(),
            inspectionJpa.getStatus()
        );
    }
}
