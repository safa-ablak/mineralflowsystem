package be.kdg.prog6;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.invoicing.domain.exception.InvoicingDomainException;
import be.kdg.prog6.landside.domain.exception.LandsideDomainException;
import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import be.kdg.prog6.waterside.domain.exception.WatersideDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the KdG MineralFlow system.
 * <p>
 * This class handles domain-specific exceptions per bounded context in accordance with
 * Domain-Driven Design (DDD) principles. Each bounded context (Landside, Warehousing, Waterside, Invoicing)
 * has its own base exception, allowing errors to be handled in isolation.
 * <p>
 * Cross-context concerns such as {@link be.kdg.prog6.common.exception.NotFoundException} and
 * {@link be.kdg.prog6.common.exception.InvalidOperationException} are also handled at the application level.
 * <p>
 * This structure ensures clear separation of concerns, proper propagation of business rule violations,
 * and supports future flexibility for context-specific error handling or logging.
 */
@ControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler(LandsideDomainException.class)
    public ResponseEntity<ErrorDto> handleLandside(final LandsideDomainException ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(WarehousingDomainException.class)
    public ResponseEntity<ErrorDto> handleWarehousing(final WarehousingDomainException ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(WatersideDomainException.class)
    public ResponseEntity<ErrorDto> handleWaterside(final WatersideDomainException ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(InvoicingDomainException.class)
    public ResponseEntity<ErrorDto> handleInvoicing(final InvoicingDomainException ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> notFound(final NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorDto> invalidOperation(final InvalidOperationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorDto(ex.getMessage()));
    }

    public record ErrorDto(String message) {
    }
}
