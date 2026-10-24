package be.kdg.prog6.invoicing.domain.exception;

import be.kdg.prog6.common.exception.NotFoundException;

import java.time.Year;

public class YearlyCommissionRateNotFoundException extends NotFoundException {
    public YearlyCommissionRateNotFoundException(final Year year) {
        super("Yearly commission rate not found for year: " + year);
    }
}
