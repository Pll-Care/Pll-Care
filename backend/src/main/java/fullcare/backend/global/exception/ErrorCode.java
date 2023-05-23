package fullcare.backend.global.exception;

import lombok.Getter;

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

}