package be.kdg.prog6.invoicing.adapter.in.web.controller;

import be.kdg.prog6.invoicing.adapter.in.web.dto.MoneyDto;
import be.kdg.prog6.invoicing.domain.Money;
import be.kdg.prog6.invoicing.port.in.usecase.query.GetTotalOutstandingCreditUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static be.kdg.prog6.common.security.UserActivityLogger.logUserActivity;

@RestController
@RequestMapping("/outstanding-credit")
public class OutstandingCreditController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OutstandingCreditController.class);

    private final GetTotalOutstandingCreditUseCase getTotalOutstandingCreditUseCase;

    public OutstandingCreditController(final GetTotalOutstandingCreditUseCase getTotalOutstandingCreditUseCase) {
        this.getTotalOutstandingCreditUseCase = getTotalOutstandingCreditUseCase;
    }

    /**
     * 📘 - User Story<br></br>
     * As an <b>accountant</b>, I want to see the total outstanding credit,
     * so that I can have visibility into the total billed-but-unpaid amounts.
     */
    @GetMapping("/total")
    @PreAuthorize("hasAnyRole('ROLE_ACCOUNTANT', 'ROLE_ADMIN')")
    public ResponseEntity<MoneyDto> getTotalOutstandingCredit(@AuthenticationPrincipal final Jwt jwt) {
        logUserActivity(LOGGER, jwt, "is requesting total outstanding credit");
        final Money total = getTotalOutstandingCreditUseCase.getTotalOutstandingCredit();
        return ResponseEntity.ok(MoneyDto.of(total));
    }
}
