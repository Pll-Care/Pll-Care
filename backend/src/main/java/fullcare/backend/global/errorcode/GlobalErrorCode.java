package fullcare.backend.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    // 400 BAD_REQUEST -> todo 각 엔티티 별로 따로따로 만들 것인가?
    // todo Controller DTO Mapping 과정에서 생기는 예외들 처리가 필요함


    // 403 FORBIDDEN
    //    권한이 없는 리소스에 접근하는 경우 -> InvalidAccessException : 403 FORBIDDEN
    INVALID_ACCESS("GLOBAL_001", HttpStatus.FORBIDDEN, "권한이 없습니다."),


    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR("GLOBAL_002", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;


}