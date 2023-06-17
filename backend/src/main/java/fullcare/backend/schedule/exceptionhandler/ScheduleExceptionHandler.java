package fullcare.backend.schedule.exceptionhandler;


import fullcare.backend.global.exception.ErrorCode;
import fullcare.backend.schedule.exceptionhandler.exception.ScheduleOutOfRangeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ScheduleExceptionHandler {
    
    @ExceptionHandler(ScheduleOutOfRangeException.class)
    public ResponseEntity<?> handleScheduleOutOfRangeException(Exception e, WebRequest request) {
        ErrorCode message = ErrorCode.getMessage(HttpStatus.BAD_REQUEST, e, request);
        return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
    }
}
