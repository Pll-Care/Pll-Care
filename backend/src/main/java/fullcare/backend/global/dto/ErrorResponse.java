package fullcare.backend.global.dto;

import fullcare.backend.global.errorcode.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String code;
    private int status;
    private LocalDateTime timestamp;
    private String message;
    private String path;

    public ErrorResponse(ErrorCode errorCode, LocalDateTime timestamp, String message, String path) {
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus().value();
        this.timestamp = timestamp;
        this.message = message;
        this.path = path;
    }

    public static ErrorResponse getResponse(ErrorCode errorCode, Exception e, HttpServletRequest request) {
        return new ErrorResponse(errorCode,
                LocalDateTime.now(),
                e.getMessage(),
                request.getServletPath());
    }

    public static ErrorResponse getResponse(ErrorCode errorCode, HttpServletRequest request) {
        return new ErrorResponse(errorCode,
                LocalDateTime.now(),
                errorCode.getMessage(),
                request.getServletPath());
    }

}
