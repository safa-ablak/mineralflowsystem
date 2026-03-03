package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.storage.Delivery;
import be.kdg.prog6.warehousing.domain.storage.Warehouse;

@FunctionalInterface
public interface DeliveryRecordedPort {
    void deliveryRecorded(Warehouse warehouse, Delivery delivery);
}
