package fullcare.backend.evaluation.exceptionhandler;

import fullcare.backend.evaluation.exceptionhandler.exception.*;
import fullcare.backend.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class EvaluationExceptionHandler {

    @ExceptionHandler(SelfEvalException.class)
    public ResponseEntity<?> handleMyEvalException(Exception e, WebRequest request) {
        ErrorCode message = ErrorCode.getMessage(HttpStatus.FORBIDDEN, e, request);
        return new ResponseEntity(message, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(DuplicateEvalException.class)
    public ResponseEntity<?> handleEvalDuplicateException(Exception e, WebRequest request) {
        ErrorCode message = ErrorCode.getMessage(HttpStatus.FORBIDDEN, e, request);
        return new ResponseEntity(message, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(NotCompletedProjectException.class)
    public ResponseEntity<?> handleEvalNotCompleteProjectException(Exception e, WebRequest request) {
        ErrorCode message = ErrorCode.getMessage(HttpStatus.FORBIDDEN, e, request);
        return new ResponseEntity(message, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(EvalOutOfRangeException.class)
    public ResponseEntity<?> handleEvalOutOfRangeException(Exception e, WebRequest request) {
        ErrorCode message = ErrorCode.getMessage(HttpStatus.BAD_REQUEST, e, request);
        return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
    }

}
