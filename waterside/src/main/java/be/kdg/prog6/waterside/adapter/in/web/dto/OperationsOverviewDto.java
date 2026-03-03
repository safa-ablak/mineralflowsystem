package be.kdg.prog6.waterside.adapter.in.web.dto;

import be.kdg.prog6.waterside.port.in.usecase.query.readmodel.OperationsOverview;

public record OperationsOverviewDto(
    String shippingOrderId,
    InspectionOperationDto inspectionOperation,
    BunkeringOperationDto bunkeringOperation,
    boolean shipReadyForLoading
) {
    public static OperationsOverviewDto from(final OperationsOverview operationsOverview) {
        return new OperationsOverviewDto(
            operationsOverview.shippingOrderId().id().toString(),
            InspectionOperationDto.fromDomain(operationsOverview.inspection()),
            BunkeringOperationDto.fromDomain(operationsOverview.bunkering()),
            operationsOverview.shipReadyForLoading()
        );
    }
}
