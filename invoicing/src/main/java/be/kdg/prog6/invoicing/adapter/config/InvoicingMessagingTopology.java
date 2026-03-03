package be.kdg.prog6.invoicing.adapter.config;

import be.kdg.prog6.common.annotation.BoundedContextMessagingConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@BoundedContextMessagingConfig
public class InvoicingMessagingTopology {
    // Outbound Exchange (None)

    // Inbound Exchange(s)
    public static final String WAREHOUSING_EVENTS_EXCHANGE = "warehousing-events";
    public static final String WATERSIDE_EVENTS_EXCHANGE = "waterside-events";

    // Queue names:
    public static final String FULFILLED_PURCHASE_ORDERS_QUEUE = "fulfilled-purchase-orders";
    public static final String INVOICING_SELLER_WAREHOUSES_STORAGE_REPORTED_QUEUE = "seller-warehouses-storage-reported";

    // Inbound routing keys (no outbound events for Invoicing)
    public static final String PURCHASE_ORDER_FULFILLED_ROUTING_KEY = "purchase-order.fulfilled";
    public static final String SELLER_WAREHOUSES_STORAGE_REPORTED_ROUTING_KEY = "seller.warehouses.storage-reported";

    // Exchange beans:
    @Bean
    TopicExchange warehousingEventsExchange() {
        return new TopicExchange(WAREHOUSING_EVENTS_EXCHANGE);
    }

    @Bean
    TopicExchange watersideEventsExchange() {
        return new TopicExchange(WATERSIDE_EVENTS_EXCHANGE);
    }

    // Queue beans:
    @Bean
    Queue fulfilledPurchaseOrdersQueue() {
        return new Queue(FULFILLED_PURCHASE_ORDERS_QUEUE, false);
    }

    @Bean
    Queue invoicingSellerWarehousesStorageReportedQueue() {
        return new Queue(INVOICING_SELLER_WAREHOUSES_STORAGE_REPORTED_QUEUE, false);
    }

    // Bindings for the queues:
    @Bean
    Binding bindingFulfilledPurchaseOrdersQueue(Queue fulfilledPurchaseOrdersQueue, TopicExchange warehousingEventsExchange) {
        return BindingBuilder
            .bind(fulfilledPurchaseOrdersQueue)
            .to(warehousingEventsExchange)
            .with(PURCHASE_ORDER_FULFILLED_ROUTING_KEY);
    }

    @Bean
    Binding bindingInvoicingSellerWarehousesStorageReportedQueue(Queue invoicingSellerWarehousesStorageReportedQueue, TopicExchange warehousingEventsExchange) {
        return BindingBuilder
            .bind(invoicingSellerWarehousesStorageReportedQueue)
            .to(warehousingEventsExchange)
            .with(SELLER_WAREHOUSES_STORAGE_REPORTED_ROUTING_KEY);
    }
}
