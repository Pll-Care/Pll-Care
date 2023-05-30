package fullcare.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Getter
public class ErrorCode {
    private int statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String path;

    public ErrorCode(int statusCode, LocalDateTime timestamp, String message, String path) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.path = path;
    }

    public static ErrorCode getMessage(HttpStatus status, Exception e, WebRequest request) {
        return new ErrorCode(
                status.value(),
                LocalDateTime.now(),
                e.getMessage(),
                request.getDescription(false).substring(4));
    }
}