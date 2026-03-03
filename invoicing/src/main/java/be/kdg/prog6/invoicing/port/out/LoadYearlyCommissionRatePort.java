package be.kdg.prog6.invoicing.port.out;

import be.kdg.prog6.invoicing.domain.YearlyCommissionRate;

// KdG gets a commission fee of 1% as decided per year contract.
@FunctionalInterface
public interface LoadYearlyCommissionRatePort {
    YearlyCommissionRate loadCurrentYearRate();
}
