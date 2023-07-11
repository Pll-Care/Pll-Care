package fullcare.backend.global.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getCode();

    HttpStatus getStatus();

    String getMessage();
}
