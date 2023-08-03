package fullcare.backend.security.jwt.exception;

import fullcare.backend.global.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {

    MALFORMED_TOKEN("JWT_001", HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다."),
    EXPIRED_TOKEN("JWT_002", HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN("JWT_003", HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 서명입니다."),
    ILLEGAL_TOKEN("JWT_004", HttpStatus.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다."),
    NOT_FOUND_USER("JWT_005", HttpStatus.UNAUTHORIZED, "등록되지 않은 사용자입니다."),

    // ! refresh token 도 만료되어 재 로그인이 필요한 경우
    AUTHENTICATION_FAIL("JWT_006", HttpStatus.UNAUTHORIZED, "재 로그인이 필요합니다."),

    // ! 토큰이 없다 -> 인증 실패
    TOKEN_NOT_FOUND("JWT_007", HttpStatus.UNAUTHORIZED, "인증에 실패했습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;

}