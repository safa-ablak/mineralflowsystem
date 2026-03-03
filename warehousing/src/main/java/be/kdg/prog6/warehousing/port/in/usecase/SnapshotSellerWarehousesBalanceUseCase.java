package be.kdg.prog6.warehousing.port.in.usecase;

import be.kdg.prog6.warehousing.port.in.command.SnapshotSellerWarehousesBalanceCommand;

@FunctionalInterface
public interface SnapshotSellerWarehousesBalanceUseCase {
    void snapshot(SnapshotSellerWarehousesBalanceCommand command);
}
