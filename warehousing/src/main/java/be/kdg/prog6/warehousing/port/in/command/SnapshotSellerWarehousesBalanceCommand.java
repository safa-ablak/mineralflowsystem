package be.kdg.prog6.warehousing.port.in.command;

import be.kdg.prog6.warehousing.domain.SellerId;

import static java.util.Objects.requireNonNull;

public record SnapshotSellerWarehousesBalanceCommand(SellerId sellerId) {
    public SnapshotSellerWarehousesBalanceCommand {
        requireNonNull(sellerId);
    }
}
