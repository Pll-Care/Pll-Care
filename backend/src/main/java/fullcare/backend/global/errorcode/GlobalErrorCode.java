package fullcare.backend.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    // ! 400 BAD_REQUEST -> todo 각 엔티티 별로 따로따로 만들 것인가?
    INVALID_DATE_RANGE("GLOBAL_004", HttpStatus.BAD_REQUEST, "시작일자와 종료일자가 올바르지 않습니다."),
    // todo Controller DTO Mapping 과정에서 생기는 예외들 처리가 필요함

    // ! 401 UNAUTHORIZED
    // OAUTH2 로그인에 실패하는 경우
    OAUTH2_AUTHENTICATION_FAIL("GLOBAL_001", HttpStatus.UNAUTHORIZED, "OAUTH2 소셜 로그인에 실패했습니다."),


    // ! 403 FORBIDDEN
    //    권한이 없는 리소스에 접근하는 경우 -> InvalidAccessException : 403 FORBIDDEN
    INVALID_ACCESS("GLOBAL_002", HttpStatus.FORBIDDEN, "권한이 없습니다."),


    // ! 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR("GLOBAL_003", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;


}