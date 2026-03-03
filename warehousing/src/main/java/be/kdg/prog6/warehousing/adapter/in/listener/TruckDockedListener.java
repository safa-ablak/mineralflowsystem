package be.kdg.prog6.warehousing.adapter.in.listener;

import be.kdg.prog6.common.event.landside.TruckDockedEvent;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.command.AssignRawMaterialToWarehouseCommand;
import be.kdg.prog6.warehousing.port.in.command.GeneratePayloadDeliveryTicketCommand;
import be.kdg.prog6.warehousing.port.in.usecase.AssignRawMaterialToWarehouseUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.GeneratePayloadDeliveryTicketUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.WAREHOUSING;
import static be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology.DOCKED_TRUCKS_QUEUE;

@Component
public class TruckDockedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TruckDockedListener.class);

    private final AssignRawMaterialToWarehouseUseCase assignRawMaterialToWarehouseUseCase;
    private final GeneratePayloadDeliveryTicketUseCase generatePayloadDeliveryTicketUseCase;

    public TruckDockedListener(final AssignRawMaterialToWarehouseUseCase assignRawMaterialToWarehouseUseCase,
                               final GeneratePayloadDeliveryTicketUseCase generatePayloadDeliveryTicketUseCase) {
        this.assignRawMaterialToWarehouseUseCase = assignRawMaterialToWarehouseUseCase;
        this.generatePayloadDeliveryTicketUseCase = generatePayloadDeliveryTicketUseCase;
    }

    @RabbitListener(queues = DOCKED_TRUCKS_QUEUE)
    public void onTruckDocked(final TruckDockedEvent event) {
        LOGGER.info(
            "Received {} at {} for a docked truck",
            event.getClass().getSimpleName(),
            WAREHOUSING
        );
        final WarehouseId warehouseId = WarehouseId.of(event.warehouseId());
        final RawMaterial rawMaterial = RawMaterial.fromString(event.rawMaterial());
        assignRawMaterialToWarehouseUseCase.assignRawMaterial(
            new AssignRawMaterialToWarehouseCommand(warehouseId, rawMaterial)
        );
        generatePayloadDeliveryTicketUseCase.generatePayloadDeliveryTicket(
            new GeneratePayloadDeliveryTicketCommand(warehouseId, event.dockNumber())
        );
    }
}
