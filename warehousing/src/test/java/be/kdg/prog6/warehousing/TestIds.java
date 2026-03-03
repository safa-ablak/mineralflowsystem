package be.kdg.prog6.warehousing;

import be.kdg.prog6.warehousing.domain.Address;
import be.kdg.prog6.warehousing.domain.BuyerId;
import be.kdg.prog6.warehousing.domain.SellerId;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderId;
import be.kdg.prog6.warehousing.domain.purchaseorder.PurchaseOrderNumber;
import be.kdg.prog6.warehousing.domain.storage.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

final class TestIds {
    public static final SellerId SELLER_ID = SellerId.of(
        UUID.fromString("bdbfd502-40d8-457d-9e20-6f9ff58d95b0")
    );
    public static final String SELLER_NAME = "Hüseyin Safa Ablak";
    public static final Address SELLER_ADDRESS = new Address(
        "Lucas Henninckstraat", "7", "Antwerp", "Belgium"
    );

    public static final BuyerId BUYER_ID = BuyerId.of(
        UUID.fromString("36c49aa2-d4cb-4fc8-a252-5a2dcf0d3f1c")
    );
    public static final String BUYER_NAME = "Thomas Maxwell";

    public static final RawMaterial RAW_MATERIAL_1 = RawMaterial.GYPSUM;
    public static final RawMaterial RAW_MATERIAL_2 = RawMaterial.IRON_ORE;
    public static final RawMaterial RAW_MATERIAL_3 = RawMaterial.CEMENT;
    public static final RawMaterial RAW_MATERIAL_4 = RawMaterial.PETCOKE;
    public static final RawMaterial RAW_MATERIAL_5 = RawMaterial.SLAG;

    public static final PurchaseOrderId PURCHASE_ORDER_ID_SINGLE_LINE = PurchaseOrderId.of(
        UUID.fromString("ed71c3f2-2302-4c47-8e8c-8c7df5425e36")
    );
    public static final PurchaseOrderId PURCHASE_ORDER_ID_FIVE_LINES = PurchaseOrderId.of(
        UUID.fromString("2d0e8c07-d420-4169-8567-9686c181cc31")
    );
    public static final PurchaseOrderNumber PURCHASE_ORDER_NUMBER_SINGLE_LINE = new PurchaseOrderNumber(
        "PO000001"
    );
    public static final PurchaseOrderNumber PURCHASE_ORDER_NUMBER_FIVE_LINES = new PurchaseOrderNumber(
        "PO000002"
    );

    public static final WarehouseId WAREHOUSE_ID_1 = WarehouseId.of(
        UUID.fromString("cb1484b5-6d12-4f57-9138-3740d9acf202")
    );
    public static final WarehouseId WAREHOUSE_ID_2 = WarehouseId.of(
        UUID.fromString("a5dd48b0-c0ee-4af8-bdd5-4e1776db45d9")
    );
    public static final WarehouseId WAREHOUSE_ID_3 = WarehouseId.of(
        UUID.fromString("2b20d3af-c5c1-48b8-a9fe-0a5780b0820c")
    );
    public static final WarehouseId WAREHOUSE_ID_4 = WarehouseId.of(
        UUID.fromString("592e1adc-dd70-409c-881e-4b60c6568646")
    );
    public static final WarehouseId WAREHOUSE_ID_5 = WarehouseId.of(
        UUID.fromString("70669b5d-d136-4853-9e95-ed792f75a3c7")
    );
    public static final WarehouseNumber WAREHOUSE_NUMBER_1 = new WarehouseNumber(
        "WH-01"
    );
    public static final WarehouseNumber WAREHOUSE_NUMBER_2 = new WarehouseNumber(
        "WH-02"
    );
    public static final WarehouseNumber WAREHOUSE_NUMBER_3 = new WarehouseNumber(
        "WH-03"
    );
    public static final WarehouseNumber WAREHOUSE_NUMBER_4 = new WarehouseNumber(
        "WH-04"
    );
    public static final WarehouseNumber WAREHOUSE_NUMBER_5 = new WarehouseNumber(
        "WH-05"
    );

    public static final DeliveryId OLDEST_DELIVERY_ID = DeliveryId.of(
        WAREHOUSE_ID_1,
        UUID.fromString("aaaaaaa1-1111-2222-3333-444444444444")
    );
    public static final DeliveryId MIDDLE_DELIVERY_ID = DeliveryId.of(
        WAREHOUSE_ID_1,
        UUID.fromString("bbbbbbb2-2222-3333-4444-555555555555")
    );
    public static final DeliveryId NEWEST_DELIVERY_ID = DeliveryId.of(
        WAREHOUSE_ID_1,
        UUID.fromString("ccccccc3-3333-4444-5555-666666666666")
    );
    public static final DeliveryId UNAGED_DELIVERY_ID = DeliveryId.of(
        WAREHOUSE_ID_1,
        UUID.fromString("ddddddd4-4444-5555-6666-777777777777")
    );

    public static final DeliveryId AFTER_REPORTING_DATE_TIME_DELIVERY_ID = DeliveryId.of(
        WAREHOUSE_ID_1,
        UUID.fromString("eeeeeee5-5555-6666-7777-888888888888")
    );

    public static final ShipmentId SHIPMENT_ID = ShipmentId.of(
        WAREHOUSE_ID_1,
        UUID.fromString("ddddddd4-4444-5555-6666-777777777777")
    );

    public static final LocalDateTime PERIOD_FROM = LocalDateTime.of(
        2025, 7, 1, 12, 0
    );
    public static final LocalDateTime PERIOD_TO = PERIOD_FROM.plusWeeks(1);

    public static final long PERIOD_DURATION_IN_DAYS = ChronoUnit.DAYS.between(
        PERIOD_FROM,
        PERIOD_TO
    );

    public static final LocalDateTime REPORTING_DATE_TIME = PERIOD_TO;

    /**
     * A valid site location within bounds.
     * Use for tests that do not care about spatial behavior.
     */
    public static final SiteLocation DEFAULT_SITE_LOCATION = new SiteLocation(0.0, 0.0);

    private TestIds() {
        throw new AssertionError("Utility class");
    }
}
