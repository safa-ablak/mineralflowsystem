package be.kdg.prog6.invoicing.adapter.out.db.adapter;

import be.kdg.prog6.invoicing.adapter.out.db.repository.YearlyCommissionRateJpaRepository;
import be.kdg.prog6.invoicing.domain.YearlyCommissionRate;
import be.kdg.prog6.invoicing.domain.exception.YearlyCommissionRateNotFoundException;
import be.kdg.prog6.invoicing.port.out.LoadYearlyCommissionRatePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class YearlyCommissionRateDatabaseAdapter implements LoadYearlyCommissionRatePort {
    private static final Logger LOGGER = LoggerFactory.getLogger(YearlyCommissionRateDatabaseAdapter.class);

    private final YearlyCommissionRateJpaRepository yearlyCommissionRateJpaRepository;

    public YearlyCommissionRateDatabaseAdapter(final YearlyCommissionRateJpaRepository yearlyCommissionRateJpaRepository) {
        this.yearlyCommissionRateJpaRepository = yearlyCommissionRateJpaRepository;
    }

    @Override
    public YearlyCommissionRate loadCurrentYearRate() {
        final Year currentYear = Year.now();
        LOGGER.info("Loading commission rate for current year {}", currentYear);
        return yearlyCommissionRateJpaRepository
            .findById(currentYear.getValue())
            .map(entity -> new YearlyCommissionRate(entity.getYear(), entity.getRate()))
            .orElseThrow(() -> YearlyCommissionRateNotFoundException.forYear(currentYear));
    }
}