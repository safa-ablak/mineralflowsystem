package be.kdg.prog6.warehousing.port.in.usecase;

import be.kdg.prog6.warehousing.port.in.command.GeneratePayloadDeliveryTicketCommand;

@FunctionalInterface
public interface GeneratePayloadDeliveryTicketUseCase {
    void generatePayloadDeliveryTicket(GeneratePayloadDeliveryTicketCommand command);
}
