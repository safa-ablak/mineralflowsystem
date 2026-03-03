package be.kdg.prog6.waterside;

import be.kdg.prog6.waterside.domain.BuyerId;
import be.kdg.prog6.waterside.domain.ShippingOrderId;

import java.time.LocalDate;
import java.util.UUID;

final class TestIds {
    public static final ShippingOrderId SHIPPING_ORDER_ID_1 = ShippingOrderId.of(
        UUID.fromString("87a0c372-64a5-4706-8a7f-2e5be218f25b")
    );
    public static final ShippingOrderId SHIPPING_ORDER_ID_2 = ShippingOrderId.of(
        UUID.fromString("fb2ce6cc-e2f2-45b2-85ee-e2d2e13e27d4")
    );
    public static final ShippingOrderId SHIPPING_ORDER_ID_3 = ShippingOrderId.of(
        UUID.fromString("1a7b597b-6b26-4209-95d1-f6d7e2957ea4")
    );
    public static final ShippingOrderId SHIPPING_ORDER_ID_4 = ShippingOrderId.of(
        UUID.fromString("38e8ecab-8bce-4e3b-a1f3-2bd43d8b66f9")
    );

    public static final BuyerId BUYER_ID = BuyerId.of(
        UUID.fromString("a3fd8652-5610-4789-8425-01d73fd1eec9")
    );

    public static final LocalDate TEST_DATE = LocalDate.of(
        2025, 7, 26
    );
    public static final LocalDate PROCESSING_DATE = TEST_DATE;
    public static final LocalDate BUNKERING_DATE = TEST_DATE;

    private TestIds() {
        throw new AssertionError("Utility class");
    }
}