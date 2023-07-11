package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemoErrorCode implements ErrorCode {

    // ! 403 Forbidden
    INVALID_ACCESS("MEMO_001", HttpStatus.FORBIDDEN, "해당 회의록에 접근 권한이 없습니다."), // -> InvalidAccessException
    INVALID_DELETE("MEMO_002", HttpStatus.FORBIDDEN, "해당 회의록에 대한 삭제 권한이 없습니다."), // -> InvalidAccessException
    INVALID_MODIFY("MEMO_003", HttpStatus.FORBIDDEN, "해당 회의록에 대한 수정 권한이 없습니다."), // -> InvalidAccessException

    // ! 404 NOT_FOUND
    MEMO_NOT_FOUND("MEMO_004", HttpStatus.NOT_FOUND, "회의록을 찾을 수 없습니다."); // -> EntityNotFoundException


    private final String code;
    private final HttpStatus status;
    private final String message;
}
