package be.kdg.prog6.landside;

import be.kdg.prog6.common.config.testsupport.TestTimeConfig;
import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.SupplierId;
import be.kdg.prog6.landside.domain.TruckLicensePlate;
import be.kdg.prog6.landside.domain.WarehouseId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

final class TestIds {
    public static final SupplierId SUPPLIER_ID = SupplierId.of(
        UUID.fromString("23f11460-22bb-4888-b52a-2c5f6c9d1ea5")
    );
    public static final WarehouseId WAREHOUSE_ID = WarehouseId.of(
        UUID.fromString("cd37c514-7cd6-4031-8130-854ffaeee00b")
    );
    public static final TruckLicensePlate TRUCK_LICENSE_PLATE = new TruckLicensePlate(
        "1-TEST-999"
    );

    public static final RawMaterial RAW_MATERIAL = RawMaterial.GYPSUM;

    public static final LocalDateTime FIXED_NOW = TestTimeConfig.FIXED_NOW;

    public static final LocalDate SCHEDULE_DATE = FIXED_NOW.toLocalDate();

    public static final LocalDateTime
        FUTURE_SCHEDULED_ARRIVAL_TIME = SCHEDULE_DATE.atTime(14, 0);
    public static final LocalDateTime
        PAST_SCHEDULED_ARRIVAL_TIME = SCHEDULE_DATE.atTime(8, 0);

    private TestIds() {
        throw new AssertionError("Utility class");
    }
}