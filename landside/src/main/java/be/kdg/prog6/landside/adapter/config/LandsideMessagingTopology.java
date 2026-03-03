package be.kdg.prog6.landside.adapter.config;

import be.kdg.prog6.common.annotation.BoundedContextMessagingConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@BoundedContextMessagingConfig
public class LandsideMessagingTopology {
    // Outbound Exchange
    public static final String LANDSIDE_EVENTS_EXCHANGE = "landside-events";
    // Inbound Exchange(s)
    public static final String WAREHOUSING_EVENTS_EXCHANGE = "warehousing-events";

    // Queue names
    public static final String GENERATED_WAREHOUSE_PDT_QUEUE = "generated-warehouse-pdt";
    public static final String RECORDED_WAREHOUSE_DELIVERIES_QUEUE = "recorded-warehouse-deliveries";
    public static final String RECORDED_WAREHOUSE_SHIPMENTS_QUEUE = "recorded-warehouse-shipments";
    public static final String RAW_MATERIAL_ASSIGNED_WAREHOUSES_QUEUE = "raw-material-assigned-warehouses";

    // Outbound routing keys
    public static final String TRUCK_DOCKED_ROUTING_KEY = "truck.docked";
    public static final String TRUCK_WEIGHED_OUT_ROUTING_KEY = "truck.weighed-out";

    // Inbound routing keys
    public static final String WAREHOUSE_PDT_GENERATED_ROUTING_KEY = "warehouse.pdt.generated";
    public static final String WAREHOUSE_DELIVERY_RECORDED_ROUTING_KEY_PATTERN = "warehouse.*.delivery.recorded";
    public static final String WAREHOUSE_SHIPMENT_RECORDED_ROUTING_KEY_PATTERN = "warehouse.*.shipment.recorded";
    public static final String WAREHOUSE_RAW_MATERIAL_ASSIGNED_ROUTING_KEY = "warehouse.raw-material.assigned";

    /*
     * Queue Beans
     */
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
}
