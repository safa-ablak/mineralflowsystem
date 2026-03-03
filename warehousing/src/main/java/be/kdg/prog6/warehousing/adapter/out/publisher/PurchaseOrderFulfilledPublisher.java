package be.kdg.prog6.warehousing.adapter.out.publisher;

import be.kdg.prog6.common.event.warehousing.PurchaseOrderFulfilledEvent;
import be.kdg.prog6.warehousing.adapter.config.WarehousingMessagingTopology;
import be.kdg.prog6.warehousing.domain.purchaseorder.OrderLine;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrder;
import be.kdg.prog6.warehousing.port.out.PurchaseOrderFulfilledPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PurchaseOrderFulfilledPublisher implements PurchaseOrderFulfilledPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderFulfilledPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public PurchaseOrderFulfilledPublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void purchaseOrderFulfilled(final PurchaseOrder purchaseOrder) {
        final String routingKey = WarehousingMessagingTopology.PURCHASE_ORDER_FULFILLED_ROUTING_KEY;
        LOGGER.info("Notifying RabbitMQ: {}", routingKey);
        final Map<String, BigDecimal> rawMaterialToAmount = mapRawMaterials(purchaseOrder);
        rabbitTemplate.convertAndSend(
            WarehousingMessagingTopology.WAREHOUSING_EVENTS_EXCHANGE,
            routingKey,
            new PurchaseOrderFulfilledEvent(
                purchaseOrder.getSellerId().id(),
                rawMaterialToAmount
            )
        );
    }

    /**
     * Maps order lines to a raw materials map<br>
     * Key:raw material ENUM name, Value:amount
     */
    private static Map<String, BigDecimal> mapRawMaterials(final PurchaseOrder purchaseOrder) {
        return purchaseOrder.getOrderLines().stream()
            .collect(Collectors.toMap(
                orderLine -> orderLine.getRawMaterial().name(),
                OrderLine::getAmount
            ));
    }
}
