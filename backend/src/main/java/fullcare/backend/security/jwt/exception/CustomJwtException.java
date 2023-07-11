package fullcare.backend.security.jwt.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomJwtException extends AuthenticationException {

    private final JwtErrorCode errorCode;

    public CustomJwtException(JwtErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
}
