package in.practice.urlshortner.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = URLExpiredException.class)
    public ResponseEntity<Error> handleException(URLExpiredException ex) {
        log.warn("URL Expired: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.GONE)
                .body(Error.builder().errorMessage(ex.getMessage()).errorCode(HttpStatus.GONE.value()).build());
    }

    @ExceptionHandler(value = URLNotFoundException.class)
    public ResponseEntity<Error> handleException(URLNotFoundException ex) {
        log.warn("URL NotFound: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Error.builder().errorMessage(ex.getMessage()).errorCode(HttpStatus.NOT_FOUND.value()).build());
    }

    @ExceptionHandler(value = ShortCodeGenerationException.class)
    public ResponseEntity<Error> handleException(ShortCodeGenerationException ex) {
        log.warn("Unable to generate ShortCode: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Error.builder().errorMessage(ex.getMessage()).errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
    }
}
