package fullcare.backend.security.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum JwtErrorCode {

    MALFORMED_TOKEN("잘못된 JWT 서명입니다."),
    EXPIRED_TOKEN("만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN("지원되지 않는 JWT 서명입니다."),
    ILLEGAL_TOKEN("JWT 토큰이 잘못되었습니다."),
    NOT_FOUND_USER("등록되지 않은 사용자입니다."),
    NOT_FOUND_TOKEN("토큰이 없습니다.")
    ;

    public final String message;

}