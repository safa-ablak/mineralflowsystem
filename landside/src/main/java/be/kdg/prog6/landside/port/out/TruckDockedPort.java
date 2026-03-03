package be.kdg.prog6.landside.port.out;

import be.kdg.prog6.common.event.landside.TruckDockedEvent;

@FunctionalInterface
public interface TruckDockedPort {
    void truckDocked(TruckDockedEvent event);
}
