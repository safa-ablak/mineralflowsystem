package be.kdg.prog6.warehousing;

import be.kdg.prog6.warehousing.adapter.out.db.entity.*;
import be.kdg.prog6.warehousing.adapter.out.db.repository.SellerJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.repository.ShipmentAllocationJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.repository.WarehouseDeliveryJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.repository.WarehouseJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.repository.WarehouseShipmentJpaRepository;
import be.kdg.prog6.warehousing.adapter.out.db.value.AddressEmbeddable;
import be.kdg.prog6.warehousing.adapter.out.db.value.SiteLocationEmbeddable;
import be.kdg.prog6.warehousing.domain.storage.RawMaterial;
import be.kdg.prog6.warehousing.port.in.query.GetWarehouseActivityHistoryQuery;
import be.kdg.prog6.warehousing.port.in.query.GetWarehouseActivityHistoryQuery.ViewMode;
import be.kdg.prog6.warehousing.port.in.usecase.query.GetWarehouseActivityHistoryUseCase;
import be.kdg.prog6.warehousing.port.in.usecase.query.readmodel.WarehouseActivityHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GetWarehouseActivityHistoryImplIntegrationTest extends AbstractDatabaseTest {
    private static final LocalDateTime BASE_TIME =
        LocalDateTime.of(2026, 1, 1, 0, 0);

    @Autowired
    private GetWarehouseActivityHistoryUseCase sut;
    @Autowired
    private WarehouseJpaRepository warehouseJpaRepository;
    @Autowired
    private WarehouseDeliveryJpaRepository warehouseDeliveryJpaRepository;
    @Autowired
    private WarehouseShipmentJpaRepository warehouseShipmentJpaRepository;
    @Autowired
    private ShipmentAllocationJpaRepository shipmentAllocationJpaRepository;
    @Autowired
    private SellerJpaRepository sellerJpaRepository;

    @BeforeEach
    void setUp() {
        shipmentAllocationJpaRepository.deleteAll();
        warehouseDeliveryJpaRepository.deleteAll();
        warehouseShipmentJpaRepository.deleteAll();
        warehouseJpaRepository.deleteAll();
        sellerJpaRepository.deleteAll();

        sellerJpaRepository.save(createSeller());
        warehouseJpaRepository.save(createWarehouse());
        warehouseDeliveryJpaRepository.save(createDelivery());
        warehouseShipmentJpaRepository.save(createShipment());
        shipmentAllocationJpaRepository.save(createAllocation());
    }

    @Test
    void shouldNotIncludeAllocationsInDefaultViewMode() {
        // Act
        final WarehouseActivityHistory history = sut.getWarehouseActivityHistory(
            new GetWarehouseActivityHistoryQuery(TestIds.WAREHOUSE_ID_1)
        );

        // Assert
        assertFalse(history.deliveries().isEmpty());
        assertFalse(history.shipments().isEmpty());
        assertTrue(history.allocations().isEmpty());
    }

    @Test
    void shouldIncludeAllocationsWhenRequested() {
        // Act
        final WarehouseActivityHistory history = sut.getWarehouseActivityHistory(
            new GetWarehouseActivityHistoryQuery(TestIds.WAREHOUSE_ID_1, ViewMode.WITH_ALLOCATIONS)
        );

        // Assert
        assertFalse(history.deliveries().isEmpty());
        assertFalse(history.shipments().isEmpty());
        assertFalse(history.allocations().isEmpty());
        assertEquals(1, history.allocations().size());
    }

    private static SellerJpaEntity createSeller() {
        final SellerJpaEntity seller = new SellerJpaEntity();
        seller.setId(TestIds.SELLER_ID.id());
        seller.setName(TestIds.SELLER_NAME);
        seller.setAddress(new AddressEmbeddable(
            TestIds.SELLER_ADDRESS.streetName(), TestIds.SELLER_ADDRESS.streetNumber(),
            TestIds.SELLER_ADDRESS.city(), TestIds.SELLER_ADDRESS.country())
        );
        return seller;
    }

    private static WarehouseJpaEntity createWarehouse() {
        final WarehouseJpaEntity warehouse = new WarehouseJpaEntity();
        warehouse.setWarehouseId(TestIds.WAREHOUSE_ID_1.id());
        warehouse.setSellerId(TestIds.SELLER_ID.id());
        warehouse.setWarehouseNumber(TestIds.WAREHOUSE_NUMBER_1.value());
        warehouse.setRawMaterial(RawMaterial.GYPSUM);
        warehouse.setBalance(BigDecimal.ZERO);
        warehouse.setPercentageFilled(BigDecimal.ZERO);
        warehouse.setBalanceUpdatedAt(BASE_TIME.minusDays(1));
        warehouse.setSiteLocation(new SiteLocationEmbeddable(0.0, 0.0));
        return warehouse;
    }

    private static WarehouseDeliveryJpaEntity createDelivery() {
        final WarehouseDeliveryJpaEntity delivery = new WarehouseDeliveryJpaEntity();
        delivery.setId(WarehouseDeliveryJpaId.of(TestIds.OLDEST_DELIVERY_ID));
        delivery.setTime(BASE_TIME);
        delivery.setAmount(BigDecimal.TEN);
        return delivery;
    }

    private static WarehouseShipmentJpaEntity createShipment() {
        final WarehouseShipmentJpaEntity shipment = new WarehouseShipmentJpaEntity();
        shipment.setId(WarehouseShipmentJpaId.of(TestIds.SHIPMENT_ID));
        shipment.setTime(BASE_TIME.plusMinutes(1));
        shipment.setAmount(BigDecimal.TEN);
        return shipment;
    }

    private static ShipmentAllocationJpaEntity createAllocation() {
        final ShipmentAllocationJpaEntity allocation = new ShipmentAllocationJpaEntity();
        allocation.setId(
            ShipmentAllocationJpaId.of(TestIds.WAREHOUSE_ID_1, TestIds.SHIPMENT_ID, TestIds.OLDEST_DELIVERY_ID)
        );
        allocation.setAmountAllocated(BigDecimal.TEN);
        return allocation;
    }
}
