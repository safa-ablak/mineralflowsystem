package be.kdg.prog6.config.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalMessagingTopology {
    /*
     * Landside Bounded Context
     */
    // For the events happening in the Landside Context
    public static final String LANDSIDE_EVENTS_EXCHANGE = "landside-events";

    // Routing keys
    public static final String TRUCK_DOCKED_ROUTING_KEY = "truck.docked";
    public static final String TRUCK_WEIGHED_OUT_ROUTING_KEY = "truck.weighed-out";

    // Queue names
    public static final String GENERATED_WAREHOUSE_PDT_QUEUE = "generated-warehouse-pdt";
    // Note: Didn't use a consistent hash exchange assuming there won't be a race condition between deliveries and shipments
    // Since this is just a demo application and not a real-world one
    public static final String RECORDED_WAREHOUSE_DELIVERIES_QUEUE = "recorded-warehouse-deliveries"; // for deliveries
    public static final String RECORDED_WAREHOUSE_SHIPMENTS_QUEUE = "recorded-warehouse-shipments"; // for shipments
    public static final String RAW_MATERIAL_ASSIGNED_WAREHOUSES_QUEUE = "raw-material-assigned-warehouses";

    /*
     * Warehousing Bounded Context
     */
    // For the events happening in the Warehousing Context
    public static final String WAREHOUSING_EVENTS_EXCHANGE = "warehousing-events";

    // Routing keys
    public static final String WAREHOUSE_PDT_GENERATED_ROUTING_KEY = "warehouse.pdt.generated";
    public static final String WAREHOUSE_DELIVERY_RECORDED_ROUTING_KEY_PATTERN = "warehouse.*.delivery.recorded";
    public static final String WAREHOUSE_SHIPMENT_RECORDED_ROUTING_KEY_PATTERN = "warehouse.*.shipment.recorded";
    public static final String WAREHOUSE_RAW_MATERIAL_ASSIGNED_ROUTING_KEY = "warehouse.raw-material.assigned";
    public static final String REFERENCED_PURCHASE_ORDER_VALIDATED_ROUTING_KEY = "referenced.purchase-order.validated";
    public static final String REFERENCED_PURCHASE_ORDER_REJECTED_ROUTING_KEY = "referenced.purchase-order.rejected";
    public static final String PURCHASE_ORDER_SHIPPED_ROUTING_KEY = "purchase-order.shipped";
    public static final String PURCHASE_ORDER_FULFILLED_ROUTING_KEY = "purchase-order.fulfilled";
    public static final String SELLER_WAREHOUSES_STORAGE_REPORTED_ROUTING_KEY = "seller.warehouses.storage-reported";

    // Queue names
    public static final String DOCKED_TRUCKS_QUEUE = "docked-trucks";
    public static final String WEIGHED_OUT_TRUCKS_QUEUE = "weighed-out-trucks";
    public static final String ENTERED_SHIPPING_ORDERS_QUEUE = "entered-shipping-orders";
    public static final String REQUESTED_SHIPPING_ORDER_CORRECTIONS_QUEUE = "requested-shipping-order-corrections";
    public static final String LOADING_INITIATED_SHIPPING_ORDERS_QUEUE = "loading-initiated-shipping-orders";
    public static final String WAREHOUSING_SELLER_WAREHOUSES_STORAGE_REPORTED_QUEUE = "seller-warehouses-storage-reported";
    public static final String DEPARTED_SHIPS_QUEUE = "departed-ships";

    /*
     * Waterside Bounded Context
     */
    // For the events happening in the Waterside Context
    public static final String WATERSIDE_EVENTS_EXCHANGE = "waterside-events";

    // Routing keys
    public static final String SHIPPING_ORDER_ENTERED_ROUTING_KEY = "shipping-order.entered";
    public static final String SHIPPING_ORDER_CORRECTION_REQUESTED_ROUTING_KEY = "shipping-order.correction.requested";
    public static final String SHIPPING_ORDER_LOADING_INITIATED_ROUTING_KEY = "shipping-order.loading.initiated";
    public static final String SHIP_DEPARTED_ROUTING_KEY = "ship.departed";

    // Queue names
    public static final String REFERENCED_PURCHASE_ORDER_VALIDATED_QUEUE = "referenced-purchase-order-validated";
    public static final String REFERENCED_PURCHASE_ORDER_REJECTED_QUEUE = "referenced-purchase-order-rejected";
    public static final String SHIPPED_PURCHASE_ORDERS_QUEUE = "shipped-purchase-orders";

    /*
     * Invoicing Bounded Context
     */
    // For the events happening in the Invoicing Context (There are no events required for the Invoicing Context as part of this project)

    // Routing keys

    // Queue names
    public static final String FULFILLED_PURCHASE_ORDERS_QUEUE = "fulfilled-purchase-orders";
    public static final String INVOICING_SELLER_WAREHOUSES_STORAGE_REPORTED_QUEUE = "seller-warehouses-storage-reported";

    /*
     * Queue Beans
     */
    // Landside:
    @Bean
    Queue generatedWarehousePDTQueue() {
        return new Queue(GENERATED_WAREHOUSE_PDT_QUEUE, false);
    }

    @Bean
    Queue recordedWarehouseDeliveriesQueue() {
        return new Queue(RECORDED_WAREHOUSE_DELIVERIES_QUEUE, false);
    }

    @Bean
    Queue recordedWarehouseShipmentsQueue() {
        return new Queue(RECORDED_WAREHOUSE_SHIPMENTS_QUEUE, false);
    }

    @Bean
    Queue rawMaterialAssignedWarehousesQueue() {
        return new Queue(RAW_MATERIAL_ASSIGNED_WAREHOUSES_QUEUE, false);
    }

    // Warehousing:
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

    // Waterside:
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

    // Invoicing:
    @Bean
    Queue fulfilledPurchaseOrdersQueue() {
        return new Queue(FULFILLED_PURCHASE_ORDERS_QUEUE, false);
    }

    @Bean
    Queue invoicingSellerWarehousesStorageReportedQueue() {
        return new Queue(INVOICING_SELLER_WAREHOUSES_STORAGE_REPORTED_QUEUE, false);
    }

    /*
     * Exchange Beans
     */
    @Bean
    TopicExchange landsideEventsExchange() {
        return new TopicExchange(LANDSIDE_EVENTS_EXCHANGE);
    }

    @Bean
    TopicExchange warehousingEventsExchange() {
        return new TopicExchange(WAREHOUSING_EVENTS_EXCHANGE);
    }

    @Bean
    TopicExchange watersideEventsExchange() {
        return new TopicExchange(WATERSIDE_EVENTS_EXCHANGE);
    }

    /*
     * Bindings for the queues
     */
    @Bean
    Binding bindingGeneratedWarehousePDTQueue(Queue generatedWarehousePDTQueue, TopicExchange warehousingEventsExchange) {
        return BindingBuilder
            .bind(generatedWarehousePDTQueue)
            .to(warehousingEventsExchange)
            .with(WAREHOUSE_PDT_GENERATED_ROUTING_KEY);
    }

    @Bean
    Binding bindingRecordedWarehouseDeliveriesQueue(Queue recordedWarehouseDeliveriesQueue, TopicExchange warehousingEventsExchange) {
        return BindingBuilder
            .bind(recordedWarehouseDeliveriesQueue)
            .to(warehousingEventsExchange)
            .with(WAREHOUSE_DELIVERY_RECORDED_ROUTING_KEY_PATTERN);
    }

    @Bean
    Binding bindingRecordedWarehouseShipmentsQueue(Queue recordedWarehouseShipmentsQueue, TopicExchange warehousingEventsExchange) {
        return BindingBuilder
            .bind(recordedWarehouseShipmentsQueue)
            .to(warehousingEventsExchange)
            .with(WAREHOUSE_SHIPMENT_RECORDED_ROUTING_KEY_PATTERN);
    }

    @Bean
    Binding bindingRawMaterialAssignedWarehousesQueue(Queue rawMaterialAssignedWarehousesQueue, TopicExchange warehousingEventsExchange) {
        return BindingBuilder
            .bind(rawMaterialAssignedWarehousesQueue)
            .to(warehousingEventsExchange)
            .with(WAREHOUSE_RAW_MATERIAL_ASSIGNED_ROUTING_KEY);
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
    Binding bindingLoadingInitiatedShippingOrdersQueue(Queue loadingInitiatedShippingOrdersQueue, TopicExchange watersideEventsExchange) {
        return BindingBuilder
            .bind(loadingInitiatedShippingOrdersQueue)
            .to(watersideEventsExchange)
            .with(SHIPPING_ORDER_LOADING_INITIATED_ROUTING_KEY);
    }

    @Bean
    Binding bindingWarehousingWarehouseStorageReportedQueue(Queue warehousingSellerWarehousesStorageReportedQueue, TopicExchange warehousingEventsExchange) {
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

    @Bean
    Binding bindingShippedPurchaseOrdersQueue(Queue shippedPurchaseOrdersQueue, TopicExchange warehousingEventsExchange) {
        return BindingBuilder
            .bind(shippedPurchaseOrdersQueue)
            .to(warehousingEventsExchange)
            .with(PURCHASE_ORDER_SHIPPED_ROUTING_KEY);
    }
}