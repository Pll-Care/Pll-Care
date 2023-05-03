package fullcare.backend.security.jwt.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomJwtException extends AuthenticationException {
    private JwtErrorCode errorCode;
    public CustomJwtException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CustomJwtException(String msg) {
        super(msg);
    }
    public CustomJwtException(String msg, JwtErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
