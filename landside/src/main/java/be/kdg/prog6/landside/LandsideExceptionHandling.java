package be.kdg.prog6.landside;

import be.kdg.prog6.common.exception.InvalidOperationException;
import be.kdg.prog6.common.exception.NotFoundException;
import be.kdg.prog6.landside.domain.exception.LandsideDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LandsideExceptionHandling {
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

    @ExceptionHandler(LandsideDomainException.class)
    public ResponseEntity<ErrorDto> handleLandside(final LandsideDomainException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorDto(ex.getMessage()));
    }

    public record ErrorDto(String message) {
    }
}
