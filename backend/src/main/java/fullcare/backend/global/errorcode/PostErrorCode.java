package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    // ! 403 Forbidden
    INVALID_ACCESS("POST_001", HttpStatus.FORBIDDEN, "해당 모집글에 접근 권한이 없습니다."), // -> InvalidAccessException
    INVALID_DELETE("POST_002", HttpStatus.FORBIDDEN, "해당 회의록에 대한 삭제 권한이 없습니다."), // -> InvalidAccessException
    INVALID_MODIFY("POST_003", HttpStatus.FORBIDDEN, "해당 회의록에 대한 수정 권한이 없습니다."), // -> InvalidAccessException

    // ! 404 NOT_FOUND
    POST_NOT_FOUND("POST_004", HttpStatus.NOT_FOUND, "모집글을 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
