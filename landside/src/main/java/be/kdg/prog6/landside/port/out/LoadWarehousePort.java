package be.kdg.prog6.landside.port.out;

import be.kdg.prog6.landside.domain.RawMaterial;
import be.kdg.prog6.landside.domain.SupplierId;
import be.kdg.prog6.landside.domain.Warehouse;
import be.kdg.prog6.landside.domain.WarehouseId;

import java.util.Optional;

public interface LoadWarehousePort {
    Optional<Warehouse> loadWarehouseById(WarehouseId id);

    Optional<Warehouse> loadWarehouseBySupplierIdAndRawMaterial(SupplierId supplierId, RawMaterial rawMaterial);
}
