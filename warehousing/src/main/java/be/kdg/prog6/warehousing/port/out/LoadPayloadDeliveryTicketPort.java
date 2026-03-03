package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.storage.PayloadDeliveryTicket;
import be.kdg.prog6.warehousing.domain.storage.WarehouseId;

import java.util.List;

@FunctionalInterface
public interface LoadPayloadDeliveryTicketPort {
    List<PayloadDeliveryTicket> loadTicketsByWarehouseId(WarehouseId warehouseId);
}
