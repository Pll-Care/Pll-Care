package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {

    SCHEDULE_NOT_FOUND("SCHEDULE_001", HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."),
    SCHEDULE_MEMBER_NOT_FOUND("SCHEDULE_002", HttpStatus.NOT_FOUND, "일정에서 해당 사용자를 찾을 수 없습니다."),

    CATEGORY_NOT_FOUND("SCHEDULE_003", HttpStatus.BAD_REQUEST, "일정 카테고리가 존재하지 않습니다."),
    INVALID_CATEGORY_MODIFY("SCHEDULE_004", HttpStatus.BAD_REQUEST, "일정 카테고리는 변경할 수 없습니다."),
    INVALID_DATE_RANGE("SCHEDULE_005", HttpStatus.BAD_REQUEST, "시작일정과 종료일정이 올바르지 않습니다."),
    INVALID_SCHEDULE_DATE_RANGE("SCHEDULE_006", HttpStatus.BAD_REQUEST, "일정이 프로젝트 기간을 벗어났습니다."),

    UNAUTHORIZED_ACCESS("SCHEDULE_007", HttpStatus.FORBIDDEN, "해당 일정에 대한 접근 권한이 없습니다."),
    UNAUTHORIZED_MODIFY("SCHEDULE_008", HttpStatus.FORBIDDEN, "해당 일정에 대한 수정 권한이 없습니다."),
    UNAUTHORIZED_DELETE("SCHEDULE_009", HttpStatus.FORBIDDEN, "해당 일정에 대한 삭제 권한이 없습니다."),

    INVALID_ACCESS("SCHEDULE_010", HttpStatus.FORBIDDEN, "완료된 프로젝트의 일정에 접근 권한이 없습니다."),
    INVALID_CREATE("SCHEDULE_011", HttpStatus.FORBIDDEN, "완료된 프로젝트는 일정을 생성할 수 없습니다."),
    INVALID_MODIFY("SCHEDULE_012", HttpStatus.FORBIDDEN, "완료된 프로젝트의 일정은 수정할 수 없습니다."),
    INVALID_DELETE("SCHEDULE_013", HttpStatus.FORBIDDEN, "완료된 프로젝트의 일정은 삭제할 수 없습니다."),

    SCHEDULE_UNCOMPLETED("SCHEDULE_014", HttpStatus.FORBIDDEN, "해당 일정이 완료되지 않았습니다."),
    SCHEDULE_COMPLETED("SCHEDULE_015", HttpStatus.FORBIDDEN, "해당 일정이 이미 완료되었습니다.");


    private final String code;
    private final HttpStatus status;
    private final String message;

}