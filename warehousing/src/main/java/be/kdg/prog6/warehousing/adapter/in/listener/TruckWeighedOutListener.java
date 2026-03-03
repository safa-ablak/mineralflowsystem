package be.kdg.prog6.warehousing.adapter.in.listener;

import be.kdg.prog6.common.event.landside.TruckWeighedOutEvent;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;
import be.kdg.prog6.warehousing.port.in.command.RecordDeliveryCommand;
import be.kdg.prog6.warehousing.port.in.usecase.RecordDeliveryUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.WAREHOUSING;
import static be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology.WEIGHED_OUT_TRUCKS_QUEUE;

@Component
public class TruckWeighedOutListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TruckWeighedOutListener.class);

    private final RecordDeliveryUseCase recordDeliveryUseCase;

    public TruckWeighedOutListener(final RecordDeliveryUseCase recordDeliveryUseCase) {
        this.recordDeliveryUseCase = recordDeliveryUseCase;
    }

    @RabbitListener(queues = WEIGHED_OUT_TRUCKS_QUEUE)
    public void onTruckWeighedOut(final TruckWeighedOutEvent event) {
        LOGGER.info(
            "Received {} at {} for a weighed out truck",
            event.getClass().getSimpleName(),
            WAREHOUSING
        );
        final RecordDeliveryCommand command = new RecordDeliveryCommand(
            WarehouseId.of(event.warehouseId()),
            event.netWeight()
        );
        recordDeliveryUseCase.recordDelivery(command);
    }
}