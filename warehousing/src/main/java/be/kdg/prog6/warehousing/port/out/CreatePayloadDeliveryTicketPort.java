package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.storage.PayloadDeliveryTicket;

@FunctionalInterface
public interface CreatePayloadDeliveryTicketPort {
    void createPayloadDeliveryTicket(PayloadDeliveryTicket payloadDeliveryTicket);
}
