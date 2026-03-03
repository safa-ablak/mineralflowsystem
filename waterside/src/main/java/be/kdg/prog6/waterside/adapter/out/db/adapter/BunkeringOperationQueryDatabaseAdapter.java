package be.kdg.prog6.waterside.adapter.out.db.adapter;

import be.kdg.prog6.waterside.adapter.out.db.repository.BunkeringOperationJpaRepository;
import be.kdg.prog6.waterside.port.out.BunkeringOperationQueryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static be.kdg.prog6.common.ProjectInfo.KDG;

@Component
public class BunkeringOperationQueryDatabaseAdapter implements BunkeringOperationQueryPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(BunkeringOperationQueryDatabaseAdapter.class);

    private final BunkeringOperationJpaRepository bunkeringOperationJpaRepository;

    public BunkeringOperationQueryDatabaseAdapter(final BunkeringOperationJpaRepository bunkeringOperationJpaRepository) {
        this.bunkeringOperationJpaRepository = bunkeringOperationJpaRepository;
    }

    @Override
    public int countPerformedBunkeringOperationsByDate(final LocalDate date) {
        LOGGER.info("Counting performed Bunkering Operations for date {} at {}", date, KDG);
        final LocalDateTime start = date.atStartOfDay();
        final LocalDateTime end = date.plusDays(1).atStartOfDay();
        final int count = (int) bunkeringOperationJpaRepository.countPerformedBetween(start, end);
        LOGGER.info("Found {} Bunkering Operations performed on: {}", count, date);
        return count;
    }
}
