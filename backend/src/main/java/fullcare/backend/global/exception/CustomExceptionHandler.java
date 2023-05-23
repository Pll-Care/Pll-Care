package fullcare.backend.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, WebRequest request) {
        ErrorCode message = getMessage(HttpStatus.INTERNAL_SERVER_ERROR, e, request);
        return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(InvalidAccessException.class)
    public ResponseEntity<?> handleInvalidAccessException(Exception e, WebRequest request) {
        ErrorCode message = getMessage(HttpStatus.UNAUTHORIZED, e, request);
        return new ResponseEntity(message, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value={MethodArgumentNotValidException.class, HttpRequestMethodNotSupportedException.class, InvalidParameterException.class, IllegalStateException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(Exception e, WebRequest request) {
        ErrorCode message = getMessage(HttpStatus.METHOD_NOT_ALLOWED, e, request);
        return new ResponseEntity(message, HttpStatus.METHOD_NOT_ALLOWED);
    }




    private static ErrorCode getMessage(HttpStatus status, Exception e, WebRequest request) {
        return new ErrorCode(
                status.value(),
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false).substring(4));
    }
}
