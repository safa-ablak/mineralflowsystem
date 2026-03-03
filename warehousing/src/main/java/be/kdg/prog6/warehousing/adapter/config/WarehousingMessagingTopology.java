package be.kdg.prog6.warehousing.adapter.config;

import be.kdg.prog6.common.annotation.BoundedContextMessagingConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@BoundedContextMessagingConfig
public class WarehousingMessagingTopology {
    // Outbound Exchange
    public static final String WAREHOUSING_EVENTS_EXCHANGE = "warehousing-events";
    // Inbound Exchange(s)
    public static final String LANDSIDE_EVENTS_EXCHANGE = "landside-events";
    public static final String WATERSIDE_EVENTS_EXCHANGE = "waterside-events";

    // Outbound routing keys
    public static final String WAREHOUSE_PDT_GENERATED_ROUTING_KEY = "warehouse.pdt.generated";
    public static final String WAREHOUSE_DELIVERY_RECORDED_ROUTING_KEY_TEMPLATE = "warehouse.%s.delivery.recorded";
    public static final String WAREHOUSE_SHIPMENT_RECORDED_ROUTING_KEY_TEMPLATE = "warehouse.%s.shipment.recorded";
    public static final String WAREHOUSE_RAW_MATERIAL_ASSIGNED_ROUTING_KEY = "warehouse.raw-material.assigned";
    public static final String REFERENCED_PURCHASE_ORDER_VALIDATED_ROUTING_KEY = "referenced.purchase-order.validated";
    public static final String REFERENCED_PURCHASE_ORDER_REJECTED_ROUTING_KEY = "referenced.purchase-order.rejected";
    public static final String PURCHASE_ORDER_SHIPPED_ROUTING_KEY = "purchase-order.shipped";
    public static final String PURCHASE_ORDER_FULFILLED_ROUTING_KEY = "purchase-order.fulfilled";
    public static final String SELLER_WAREHOUSES_STORAGE_REPORTED_ROUTING_KEY = "seller.warehouses.storage-reported";

    // Inbound routing keys
    public static final String TRUCK_DOCKED_ROUTING_KEY = "truck.docked";
    public static final String TRUCK_WEIGHED_OUT_ROUTING_KEY = "truck.weighed-out";
    public static final String SHIPPING_ORDER_ENTERED_ROUTING_KEY = "shipping-order.entered";
    public static final String SHIPPING_ORDER_CORRECTION_REQUESTED_ROUTING_KEY = "shipping-order.correction.requested";
    public static final String SHIPPING_ORDER_LOADING_INITIATED_ROUTING_KEY = "shipping-order.loading.initiated";
    public static final String SHIP_DEPARTED_ROUTING_KEY = "ship.departed";

    // Queue names
    public static final String DOCKED_TRUCKS_QUEUE = "docked-trucks";
    public static final String WEIGHED_OUT_TRUCKS_QUEUE = "weighed-out-trucks";
    public static final String ENTERED_SHIPPING_ORDERS_QUEUE = "entered-shipping-orders";
    public static final String REQUESTED_SHIPPING_ORDER_CORRECTIONS_QUEUE = "requested-shipping-order-corrections";
    public static final String LOADING_INITIATED_SHIPPING_ORDERS_QUEUE = "loading-initiated-shipping-orders";
    public static final String WAREHOUSING_SELLER_WAREHOUSES_STORAGE_REPORTED_QUEUE = "seller-warehouses-storage-reported";
    public static final String DEPARTED_SHIPS_QUEUE = "departed-ships";

    // Exchange beans:
    @Bean
    TopicExchange warehousingEventsExchange() {
        return new TopicExchange(WAREHOUSING_EVENTS_EXCHANGE);
    }

    @Bean
    TopicExchange landsideEventsExchange() {
        return new TopicExchange(LANDSIDE_EVENTS_EXCHANGE);
    }

    @Bean
    TopicExchange watersideEventsExchange() {
        return new TopicExchange(WATERSIDE_EVENTS_EXCHANGE);
    }

    // Queue beans:
    @Bean
    Queue dockedTrucksQueue() {
        return new Queue(DOCKED_TRUCKS_QUEUE, false);
    }

    @Bean
    Queue weighedOutTrucksQueue() {
        return new Queue(WEIGHED_OUT_TRUCKS_QUEUE, false);
    }

    @Bean
    Queue enteredShippingOrdersQueue() {
        return new Queue(ENTERED_SHIPPING_ORDERS_QUEUE, false);
    }

    @Bean
    Queue requestedShippingOrderCorrectionsQueue() {
        return new Queue(REQUESTED_SHIPPING_ORDER_CORRECTIONS_QUEUE, false);
    }

    @Bean
    Queue loadingInitiatedShippingOrdersQueue() {
        return new Queue(LOADING_INITIATED_SHIPPING_ORDERS_QUEUE, false);
    }

    @Bean
    Queue warehousingSellerWarehousesStorageReportedQueue() {
        return new Queue(WAREHOUSING_SELLER_WAREHOUSES_STORAGE_REPORTED_QUEUE, false);
    }

    @Bean
    Queue departedShipsQueue() {
        return new Queue(DEPARTED_SHIPS_QUEUE, false);
    }

    // Bindings for the queues:
    @Bean
    Binding bindingDockedTrucksQueue(Queue dockedTrucksQueue, TopicExchange landsideEventsExchange) {
        return BindingBuilder
            .bind(dockedTrucksQueue)
            .to(landsideEventsExchange)
            .with(TRUCK_DOCKED_ROUTING_KEY);
    }

    @Bean
    Binding bindingWeighedOutTrucksQueue(Queue weighedOutTrucksQueue, TopicExchange landsideEventsExchange) {
        return BindingBuilder
            .bind(weighedOutTrucksQueue)
            .to(landsideEventsExchange)
            .with(TRUCK_WEIGHED_OUT_ROUTING_KEY);
    }

    @Bean
    Binding bindingEnteredShippingOrdersQueue(Queue enteredShippingOrdersQueue, TopicExchange watersideEventsExchange) {
        return BindingBuilder
            .bind(enteredShippingOrdersQueue)
            .to(watersideEventsExchange)
            .with(SHIPPING_ORDER_ENTERED_ROUTING_KEY);
    }

    @Bean
    Binding bindingRequestedShippingOrderCorrectionsQueue(Queue requestedShippingOrderCorrectionsQueue, TopicExchange watersideEventsExchange) {
        return BindingBuilder
            .bind(requestedShippingOrderCorrectionsQueue)
            .to(watersideEventsExchange)
            .with(SHIPPING_ORDER_CORRECTION_REQUESTED_ROUTING_KEY);
    }

    @Bean
    Binding bindingLoadingInitiatedShippingOrdersQueue(Queue loadingInitiatedShippingOrdersQueue, TopicExchange watersideEventsExchange) {
        return BindingBuilder
            .bind(loadingInitiatedShippingOrdersQueue)
            .to(watersideEventsExchange)
            .with(SHIPPING_ORDER_LOADING_INITIATED_ROUTING_KEY);
    }

    @Bean
    Binding bindingWarehousingSellerWarehousesStorageReportedQueue(Queue warehousingSellerWarehousesStorageReportedQueue, TopicExchange warehousingEventsExchange) {
        return BindingBuilder
            .bind(warehousingSellerWarehousesStorageReportedQueue)
            .to(warehousingEventsExchange)
            .with(SELLER_WAREHOUSES_STORAGE_REPORTED_ROUTING_KEY);
    }

    @Bean
    Binding bindingDepartedShipsQueue(Queue departedShipsQueue, TopicExchange watersideEventsExchange) {
        return BindingBuilder
            .bind(departedShipsQueue)
            .to(watersideEventsExchange)
            .with(SHIP_DEPARTED_ROUTING_KEY);
    }
}
