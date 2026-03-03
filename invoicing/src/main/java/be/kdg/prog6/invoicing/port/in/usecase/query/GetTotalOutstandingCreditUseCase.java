package be.kdg.prog6.invoicing.port.in.usecase.query;

import be.kdg.prog6.invoicing.domain.Money;

@FunctionalInterface
public interface GetTotalOutstandingCreditUseCase {
    Money getTotalOutstandingCredit();
}
