package be.kdg.prog6.warehousing;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.warehousing.domain.exception.WarehousingDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WarehousingExceptionHandling {
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

    @ExceptionHandler(WarehousingDomainException.class)
    public ResponseEntity<ErrorDto> handleWarehousing(final WarehousingDomainException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorDto(ex.getMessage()));
    }

    public record ErrorDto(String message) {
    }
}
