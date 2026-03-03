package be.kdg.prog6.waterside.adapter.in.web.dto;

import be.kdg.prog6.waterside.domain.InspectionOperation;

import java.time.LocalDate;

public record InspectionOperationDto(
    LocalDate performedOn,
    String inspectorSignature,
    String status
) {
    public static InspectionOperationDto fromDomain(final InspectionOperation inspectionOperation) {
        return new InspectionOperationDto(
            inspectionOperation.getPerformedOn(),
            inspectionOperation.getInspectorSignature(),
            inspectionOperation.getStatus().name()
        );
    }
}
