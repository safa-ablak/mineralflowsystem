package be.kdg.prog6.landside.port.out;

import be.kdg.prog6.landside.domain.event.TruckDepartedEvent;

@FunctionalInterface
public interface TruckDepartedPort {
    void truckDeparted(TruckDepartedEvent event);
}
