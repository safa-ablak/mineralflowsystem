package be.kdg.prog6.landside.adapter.out.db.adapter;

import be.kdg.prog6.landside.adapter.out.db.entity.WarehouseJpaEntity;
import be.kdg.prog6.landside.adapter.out.db.repository.WarehouseJpaRepository;
import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.SupplierId;
import be.kdg.prog6.landside.domain.Warehouse;
import be.kdg.prog6.landside.domain.WarehouseId;
import be.kdg.prog6.landside.port.out.LoadWarehousePort;
import be.kdg.prog6.landside.port.out.UpdateWarehousePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("landsideWarehouseDatabaseAdapter")
public class WarehouseDatabaseAdapter implements LoadWarehousePort, UpdateWarehousePort {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseDatabaseAdapter.class);

    private final WarehouseJpaRepository warehouseJpaRepository;

    public WarehouseDatabaseAdapter(final WarehouseJpaRepository warehouseJpaRepository) {
        this.warehouseJpaRepository = warehouseJpaRepository;
    }

    @Override
    public Optional<Warehouse> loadWarehouseById(final WarehouseId id) {
        return warehouseJpaRepository.findByWarehouseId(id.id()).map(this::toWarehouse);
    }

    // Note: For the sake of simplicity, we assume that a supplier can only have at most one warehouse per raw material type.
    // -> Optional<>
    @Override
    public Optional<Warehouse> loadWarehouseBySupplierIdAndRawMaterial(
        final SupplierId supplierId,
        final RawMaterial rawMaterial
    ) {
        LOGGER.info("Loading Warehouse by Supplier ID {} and Raw Material {}",
            supplierId.id(), rawMaterial
        );
        return warehouseJpaRepository
            .findBySupplierIdAndRawMaterial(supplierId.id(), rawMaterial).map(this::toWarehouse);
    }

    public Warehouse toWarehouse(final WarehouseJpaEntity warehouseJpaEntity) {
        return new Warehouse(
            WarehouseId.of(warehouseJpaEntity.getWarehouseId()),
            warehouseJpaEntity.isAvailable(),
            warehouseJpaEntity.getRawMaterial(),
            SupplierId.of(warehouseJpaEntity.getSupplierId())
        );
    }

    /**
     * Updates warehouse fields. Assumes the warehouse already exists, as all warehouses
     * are preloaded via {@code data.sql}. Therefore, {@code orElseThrow()} is used instead
     * of creating a new Warehouse.
     */
    @Override
    public void updateWarehouse(final Warehouse warehouse) {
        final UUID id = warehouse.getWarehouseId().id();
        final WarehouseJpaEntity warehouseJpaEntity = warehouseJpaRepository.findByWarehouseId(id).orElseThrow();
        warehouseJpaEntity.setAvailable(warehouse.isAvailable());
        warehouseJpaEntity.setRawMaterial(warehouse.getRawMaterial());
        warehouseJpaEntity.setSupplierId(warehouse.getSupplierId().id());
        LOGGER.info("Updating Warehouse with ID {}", id);
        warehouseJpaRepository.save(warehouseJpaEntity);
    }
}
