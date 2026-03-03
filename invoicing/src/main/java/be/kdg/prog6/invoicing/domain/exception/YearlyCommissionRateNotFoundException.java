package be.kdg.prog6.invoicing.domain.exception;

import be.kdg.prog6.common.exception.NotFoundException;

import java.time.Year;

public class YearlyCommissionRateNotFoundException extends NotFoundException {
    private YearlyCommissionRateNotFoundException(String message) {
        super(message);
    }

    public static YearlyCommissionRateNotFoundException forYear(final Year year) {
        return new YearlyCommissionRateNotFoundException(
            "Yearly commission rate not found for year: " + year
        );
    }
}
