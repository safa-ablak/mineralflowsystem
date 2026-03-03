package be.kdg.prog6.waterside.adapter.config;

import be.kdg.prog6.common.annotation.BoundedContextMessagingConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@BoundedContextMessagingConfig
public class WatersideMessagingTopology {
    // Outbound Exchange
    public static final String WATERSIDE_EVENTS_EXCHANGE = "waterside-events";
    // Inbound Exchange(s)
    public static final String WAREHOUSING_EVENTS_EXCHANGE = "warehousing-events";

    // Queue names
    public static final String REFERENCED_PURCHASE_ORDER_VALIDATED_QUEUE = "referenced-purchase-order-validated";
    public static final String REFERENCED_PURCHASE_ORDER_REJECTED_QUEUE = "referenced-purchase-order-rejected";
    public static final String SHIPPED_PURCHASE_ORDERS_QUEUE = "shipped-purchase-orders";

    // Outbound routing keys
    public static final String SHIPPING_ORDER_ENTERED_ROUTING_KEY = "shipping-order.entered";
    public static final String SHIPPING_ORDER_CORRECTION_REQUESTED_ROUTING_KEY = "shipping-order.correction.requested";
    public static final String SHIPPING_ORDER_LOADING_INITIATED_ROUTING_KEY = "shipping-order.loading.initiated";
    public static final String SHIP_DEPARTED_ROUTING_KEY = "ship.departed";

    // Inbound routing keys
    public static final String REFERENCED_PURCHASE_ORDER_VALIDATED_ROUTING_KEY = "referenced.purchase-order.validated";
    public static final String REFERENCED_PURCHASE_ORDER_REJECTED_ROUTING_KEY = "referenced.purchase-order.rejected";
    public static final String PURCHASE_ORDER_SHIPPED_ROUTING_KEY = "purchase-order.shipped";

    @Bean
    Queue referencedPurchaseOrderValidatedQueue() {
        return new Queue(REFERENCED_PURCHASE_ORDER_VALIDATED_QUEUE, false);
    }

    @Bean
    Queue referencedPurchaseOrderRejectedQueue() {
        return new Queue(REFERENCED_PURCHASE_ORDER_REJECTED_QUEUE, false);
    }

    @Bean
    Queue shippedPurchaseOrdersQueue() {
        return new Queue(SHIPPED_PURCHASE_ORDERS_QUEUE, false);
    }

    @Bean
    TopicExchange watersideEventsExchange() {
        return new TopicExchange(WATERSIDE_EVENTS_EXCHANGE);
    }

    @Bean
    TopicExchange warehousingEventsExchange() {
        return new TopicExchange(WAREHOUSING_EVENTS_EXCHANGE);
    }

    @Bean
    Binding bindingReferencedPurchaseOrderValidatedQueue(Queue referencedPurchaseOrderValidatedQueue, TopicExchange warehousingEventsExchange) {
        return BindingBuilder
            .bind(referencedPurchaseOrderValidatedQueue)
            .to(warehousingEventsExchange)
            .with(REFERENCED_PURCHASE_ORDER_VALIDATED_ROUTING_KEY);
    }

    @Bean
    Binding bindingReferencedPurchaseOrderRejectedQueue(Queue referencedPurchaseOrderRejectedQueue, TopicExchange warehousingEventsExchange) {
        return BindingBuilder
            .bind(referencedPurchaseOrderRejectedQueue)
            .to(warehousingEventsExchange)
            .with(REFERENCED_PURCHASE_ORDER_REJECTED_ROUTING_KEY);
    }

    @Bean
    Binding bindingShippedPurchaseOrdersQueue(Queue shippedPurchaseOrdersQueue, TopicExchange warehousingEventsExchange) {
        return BindingBuilder
            .bind(shippedPurchaseOrdersQueue)
            .to(warehousingEventsExchange)
            .with(PURCHASE_ORDER_SHIPPED_ROUTING_KEY);
    }
}
