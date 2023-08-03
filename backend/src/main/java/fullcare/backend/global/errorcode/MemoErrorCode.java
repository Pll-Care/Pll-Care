package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemoErrorCode implements ErrorCode {

    MEMO_NOT_FOUND("MEMO_001", HttpStatus.NOT_FOUND, "회의록을 찾을 수 없습니다."),

    UNAUTHORIZED_ACCESS("MEMO_002", HttpStatus.FORBIDDEN, "해당 회의록에 대한 접근 권한이 없습니다."), // ? unused
    UNAUTHORIZED_CREATE("MEMO_003", HttpStatus.FORBIDDEN, "해당 프로젝트에 대한 회의록 생성 권한이 없습니다."), // ? unused
    UNAUTHORIZED_MODIFY("MEMO_004", HttpStatus.FORBIDDEN, "해당 회의록에 대한 수정 권한이 없습니다."), // ? unused
    UNAUTHORIZED_DELETE("MEMO_005", HttpStatus.FORBIDDEN, "해당 회의록에 대한 삭제 권한이 없습니다."),

    INVALID_ACCESS("MEMO_006", HttpStatus.FORBIDDEN, "완료된 프로젝트의 회의록에 접근 권한이 없습니다."), // ? unused
    INVALID_CREATE("MEMO_007", HttpStatus.FORBIDDEN, "완료된 프로젝트는 회의록을 생성할 수 없습니다."),
    INVALID_MODIFY("MEMO_008", HttpStatus.FORBIDDEN, "완료된 프로젝트의 회의록은 수정할 수 없습니다."),
    INVALID_DELETE("MEMO_009", HttpStatus.FORBIDDEN, "완료된 프로젝트의 회의록은 삭제할 수 없습니다."),
    INVALID_BOOKMARK("MEMO_010", HttpStatus.FORBIDDEN, "완료된 프로젝트는 회의록을 북마크할 수 없습니다.");


    private final String code;
    private final HttpStatus status;
    private final String message;
}
