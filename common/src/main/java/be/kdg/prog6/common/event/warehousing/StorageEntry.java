package be.kdg.prog6.common.event.warehousing;

import java.math.BigDecimal;

// ID of the sourced delivery is omitted, because in the Invoicing BC we're just interested in:
// - How much of it is remaining
// - How many days it has been sitting at the warehouse
public record StorageEntry(
    BigDecimal remainingAmount,
    int daysStored
) {
}