package be.kdg.prog6.landside.adapter.in.listener;

import be.kdg.prog6.common.event.warehousing.PayloadDeliveryTicketGeneratedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static be.kdg.prog6.common.BoundedContext.LANDSIDE;
import static be.kdg.prog6.landside.adapter.config.LandsideMessagingTopology.GENERATED_WAREHOUSE_PDT_QUEUE;

@Component
public class PayloadDeliveryTicketGeneratedListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayloadDeliveryTicketGeneratedListener.class);
    private static final String PDT_COPY_TEMPLATE = """
        ╔══════════════════════════════════════════════════════════╗
        ║           == Payload Delivery Ticket (Copy) ==           ║
        ╠══════════════════════════════════════════════════════════╣
        ║ Ticket ID        :  %-36s ║
        ║ Warehouse Nr     :  %-36s ║
        ║ Raw Material     :  %-36s ║
        ║ Generation Time  :  %-36s ║
        ║ Dock Nr          :  %-36s ║
        ╚══════════════════════════════════════════════════════════╝
        """;

    /*
     * The Landside BC will be listening to the `PayloadDeliveryTicketGeneratedEvent` and to keep things simple
     * I am logging this in the console as the "Copy of the Payload Delivery Ticket" the truck driver receives.
     */
    @RabbitListener(queues = GENERATED_WAREHOUSE_PDT_QUEUE)
    public void onPayloadDeliveryTicketGenerated(final PayloadDeliveryTicketGeneratedEvent event) {
        LOGGER.info(
            """
                Received {} at {}
                
                {}
                """,
            event.getClass().getSimpleName(),
            LANDSIDE,
            formatPayloadDeliveryTicketCopy(event)
        );
    }

    private static String formatPayloadDeliveryTicketCopy(final PayloadDeliveryTicketGeneratedEvent event) {
        return PDT_COPY_TEMPLATE
            .formatted(
                event.ticketId().toString(),
                event.warehouseNumber(),
                event.rawMaterial(),
                event.generationTime().toString(),
                event.dockNumber()
            );
    }
}