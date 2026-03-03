package be.kdg.prog6.warehousing.port.in.usecase;

import be.kdg.prog6.warehousing.port.in.command.RecordDeliveryCommand;

@FunctionalInterface
public interface RecordDeliveryUseCase {
    void recordDelivery(RecordDeliveryCommand command);
}
