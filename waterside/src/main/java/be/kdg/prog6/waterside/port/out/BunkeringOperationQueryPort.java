package be.kdg.prog6.waterside.port.out;

import java.time.LocalDate;

public interface BunkeringOperationQueryPort {
    int countPerformedBunkeringOperationsByDate(LocalDate date);
}
