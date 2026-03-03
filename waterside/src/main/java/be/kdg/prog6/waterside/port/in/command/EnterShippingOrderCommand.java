package be.kdg.prog6.waterside.port.in.command;

import be.kdg.prog6.waterside.domain.BuyerId;
import be.kdg.prog6.waterside.domain.ReferenceId;
import be.kdg.prog6.waterside.domain.VesselNumber;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

public record EnterShippingOrderCommand(
    BuyerId buyerId,
    ReferenceId referenceId, // The ID reference for the Purchase Order
    VesselNumber vesselNumber,
    LocalDateTime scheduledArrivalDate,
    LocalDateTime scheduledDepartureDate
) {
    public EnterShippingOrderCommand {
        requireNonNull(buyerId);
        requireNonNull(referenceId);
        requireNonNull(vesselNumber);
        requireNonNull(scheduledArrivalDate);
        requireNonNull(scheduledDepartureDate);
    }
}
