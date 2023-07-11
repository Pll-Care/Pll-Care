package fullcare.backend.global.exceptionhandling.exceptionhandler;

import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.errorcode.GlobalErrorCode;
import fullcare.backend.global.exceptionhandling.exception.RestApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // * 모든 REST API 예외 발생 처리
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<?> handleRestApiException(RestApiException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.getResponse(e.getErrorCode(), e, request);
        return new ResponseEntity<>(errorResponse, e.getErrorCode().getStatus());
    }

    // * 스프링 프레임워크단에서 발생하는 예외 처리
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, HttpRequestMethodNotSupportedException.class, InvalidParameterException.class, IllegalStateException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(Exception e, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.getResponse(GlobalErrorCode.INTERNAL_SERVER_ERROR, e, request);
        return new ResponseEntity(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);

    }


    // * 그 외의 예상하지 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.getResponse(GlobalErrorCode.INTERNAL_SERVER_ERROR, e, request);
        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
