package be.kdg.prog6.landside.adapter.in.listener;

import be.kdg.prog6.common.event.warehousing.RawMaterialAssignedToWarehouseEvent;
import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.WarehouseId;
import be.kdg.prog6.landside.port.in.command.CancelMismatchedAppointmentsForWarehouseCommand;
import be.kdg.prog6.landside.port.in.command.ProjectRawMaterialCommand;
import be.kdg.prog6.landside.port.in.projector.WarehouseRawMaterialProjector;
import be.kdg.prog6.landside.port.in.usecase.CancelMismatchedAppointmentsForWarehouseUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.LANDSIDE;
import static be.kdg.prog6.landside.adapter.config.LandsideMessagingTopology.RAW_MATERIAL_ASSIGNED_WAREHOUSES_QUEUE;

@Component
public class RawMaterialAssignedToWarehouseListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RawMaterialAssignedToWarehouseListener.class);

    private final WarehouseRawMaterialProjector warehouseRawMaterialProjector;
    private final CancelMismatchedAppointmentsForWarehouseUseCase cancelMismatchedAppointmentsForWarehouseUseCase;

    public RawMaterialAssignedToWarehouseListener(final WarehouseRawMaterialProjector warehouseRawMaterialProjector,
                                                  final CancelMismatchedAppointmentsForWarehouseUseCase cancelMismatchedAppointmentsForWarehouseUseCase) {
        this.warehouseRawMaterialProjector = warehouseRawMaterialProjector;
        this.cancelMismatchedAppointmentsForWarehouseUseCase = cancelMismatchedAppointmentsForWarehouseUseCase;
    }

    @RabbitListener(queues = RAW_MATERIAL_ASSIGNED_WAREHOUSES_QUEUE)
    public void onRawMaterialAssigned(final RawMaterialAssignedToWarehouseEvent event) {
        LOGGER.info(
            "Received {} at {} for Warehouse {}",
            event.getClass().getSimpleName(),
            LANDSIDE,
            event.warehouseId()
        );
        final WarehouseId warehouseId = WarehouseId.of(event.warehouseId());
        final RawMaterial rawMaterial = RawMaterial.fromString(event.rawMaterial());
        warehouseRawMaterialProjector.projectRawMaterial(
            new ProjectRawMaterialCommand(warehouseId, rawMaterial)
        );
        cancelMismatchedAppointmentsForWarehouseUseCase.cancelAppointmentsWithMismatchedRawMaterial(
            new CancelMismatchedAppointmentsForWarehouseCommand(warehouseId, rawMaterial)
        );
    }
}
