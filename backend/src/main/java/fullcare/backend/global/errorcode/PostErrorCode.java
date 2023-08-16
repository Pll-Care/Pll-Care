package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND("POST_001", HttpStatus.NOT_FOUND, "모집글을 찾을 수 없습니다."),

    INVALID_DATE_RANGE("POST_002", HttpStatus.BAD_REQUEST, "시작일자와 종료일자가 올바르지 않습니다."),
    INVALID_RECRUITMENT_DATE_RANGE("POST_003", HttpStatus.BAD_REQUEST, "모집 일정이 프로젝트 기간을 벗어났습니다."),

    UNAUTHORIZED_ACCESS("POST_004", HttpStatus.FORBIDDEN, "해당 모집글에 대한 접근 권한이 없습니다."), // ? unused
    UNAUTHORIZED_CREATE("POST_005", HttpStatus.FORBIDDEN, "해당 프로젝트에 대한 모집글 생성 권한이 없습니다."), // ? unused
    UNAUTHORIZED_MODIFY("POST_006", HttpStatus.FORBIDDEN, "해당 모집글에 대한 수정 권한이 없습니다."),
    UNAUTHORIZED_DELETE("POST_007", HttpStatus.FORBIDDEN, "해당 모집글에 대한 삭제 권한이 없습니다."),


    INVALID_ACCESS("POST_008", HttpStatus.FORBIDDEN, "완료된 프로젝트의 모집글에 접근 권한이 없습니다."), // ? unused
    INVALID_CREATE("POST_009", HttpStatus.FORBIDDEN, "완료된 프로젝트는 모집글을 생성할 수 없습니다."),
    INVALID_MODIFY("POST_010", HttpStatus.FORBIDDEN, "완료된 프로젝트의 모집글은 수정할 수 없습니다."),
    INVALID_DELETE("POST_011", HttpStatus.FORBIDDEN, "완료된 프로젝트의 모집글은 삭제할 수 없습니다."),


    APPLY_NOT_FOUND("APPLY_001", HttpStatus.NOT_FOUND, "지원 정보를 찾을 수 없습니다."),
    DUPLICATE_APPLY("APPLY_002", HttpStatus.FORBIDDEN, "이미 프로젝트에 지원한 사용자입니다."),


    RECRUITMENT_NOT_FOUND("RECRUITMENT_001", HttpStatus.BAD_REQUEST, "모집 중인 포지션이 아닙니다."),
    INVALID_RECRUITMENT_AMOUNT("RECRUITMENT_002", HttpStatus.BAD_REQUEST, "모집인원 정보가 올바르지 않습니다."),
    RECRUITMENT_COMPLETED("RECRUITMENT_003", HttpStatus.FORBIDDEN, "이미 모집이 완료된 포지션입니다.");


    private final String code;
    private final HttpStatus status;
    private final String message;
}
