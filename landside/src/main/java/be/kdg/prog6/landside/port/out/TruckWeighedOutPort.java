package be.kdg.prog6.landside.port.out;

import be.kdg.prog6.common.event.landside.TruckWeighedOutEvent;

@FunctionalInterface
public interface TruckWeighedOutPort {
    void truckWeighedOut(TruckWeighedOutEvent event);
}
