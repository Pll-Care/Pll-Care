package fullcare.backend.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    // ! 401 UNAUTHORIZED
    // OAUTH2 로그인에 실패하는 경우
    OAUTH2_AUTHENTICATION_FAIL("GLOBAL_001", HttpStatus.UNAUTHORIZED, "OAUTH2 소셜 로그인에 실패했습니다."),

    // ! 404 NOT FOUND
    RESOURCE_NOT_FOUND("GLOBAL_002", HttpStatus.NOT_FOUND, "요청 데이터를 찾을 수 없습니다."),

    // ! 400 BAD REQUEST
    INVALID_REQUEST_DATA("GLOBAL_003", HttpStatus.BAD_REQUEST, "요청 데이터가 올바르지 않습니다."),

    // ! 403 FORBIDDEN
    //    권한이 없는 리소스에 접근하는 경우 -> InvalidAccessException : 403 FORBIDDEN
    UNAUTHORIZED_ACCESS("GLOBAL_004", HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INVALID_ACCESS("GLOBAL_005", HttpStatus.FORBIDDEN, "비정상적인 API 요청입니다."),

    // ! 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR("GLOBAL_006", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;


}