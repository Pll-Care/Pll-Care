package fullcare.backend.global.exceptionhandling.exceptionhandler;

import com.fasterxml.jackson.databind.DatabindException;
import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.errorcode.GlobalErrorCode;
import fullcare.backend.global.exceptionhandling.exception.RestApiException;
import fullcare.backend.security.jwt.exception.CustomJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // * 모든 REST API 예외 발생 처리
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<?> handleRestApiException(RestApiException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.getResponse(e.getErrorCode(), request);
        return new ResponseEntity<>(errorResponse, e.getErrorCode().getStatus());
    }

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<?> handleCustomJwtException(CustomJwtException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.getResponse(e.getErrorCode(), request);
        return new ResponseEntity<>(errorResponse, e.getErrorCode().getStatus());
    }

    // * 스프링 프레임워크단에서 발생하는 예외 처리
    @ExceptionHandler(value = {MethodArgumentNotValidException.class,})
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.getResponse(GlobalErrorCode.INVALID_REQUEST_DATA, e, request);
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.getResponse(GlobalErrorCode.INVALID_REQUEST_DATA, e, request);
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.getResponse(GlobalErrorCode.INVALID_ACCESS, e, request);
        return new ResponseEntity(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = {DatabindException.class})
    public ResponseEntity<?> handleDataBindException(DatabindException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.getResponse(GlobalErrorCode.INVALID_REQUEST_DATA, e, request);
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<?> handleBindException(BindException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.getResponse(GlobalErrorCode.INVALID_REQUEST_DATA, e, request);
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ServletRequestBindingException.class})
    public ResponseEntity<?> handleServletRequestBindingException(ServletRequestBindingException e, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.getResponse(GlobalErrorCode.INVALID_REQUEST_DATA, e, request);
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }


    // * 그 외의 예상하지 못한 서버 내부 동작 도중 발생하는 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        ErrorResponse errorResponse = ErrorResponse.getResponse(GlobalErrorCode.INTERNAL_SERVER_ERROR, e, request);
        return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
