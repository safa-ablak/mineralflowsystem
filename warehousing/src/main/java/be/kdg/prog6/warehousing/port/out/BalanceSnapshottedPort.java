package be.kdg.prog6.warehousing.port.out;

import be.kdg.prog6.warehousing.domain.storage.Warehouse;

@FunctionalInterface
public interface BalanceSnapshottedPort {
    void balanceSnapshotted(Warehouse warehouse);
}
