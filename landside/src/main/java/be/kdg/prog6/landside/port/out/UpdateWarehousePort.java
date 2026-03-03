package be.kdg.prog6.landside.port.out;

import be.kdg.prog6.landside.domain.Warehouse;

@FunctionalInterface
public interface UpdateWarehousePort {
    void updateWarehouse(Warehouse warehouse);
}
