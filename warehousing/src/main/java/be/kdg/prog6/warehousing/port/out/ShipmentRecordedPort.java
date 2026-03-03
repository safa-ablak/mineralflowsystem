package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.storage.ShipmentRecord;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;

@FunctionalInterface
public interface ShipmentRecordedPort {
    void shipmentRecorded(Warehouse warehouse, ShipmentRecord shipmentRecord);
}
