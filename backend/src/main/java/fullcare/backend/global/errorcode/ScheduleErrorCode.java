package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {

    // ! 403 Forbidden
    INVALID_ACCESS("SCHEDULE_001", HttpStatus.FORBIDDEN, "해당 일정에 접근 권한이 없습니다."), // -> InvalidAccessException
    INVALID_DELETE("SCHEDULE_002", HttpStatus.FORBIDDEN, "해당 일정에 대한 삭제 권한이 없습니다."), // -> InvalidAccessException
    INVALID_MODIFY("SCHEDULE_003", HttpStatus.FORBIDDEN, "해당 일정에 대한 수정 권한이 없습니다."), // -> InvalidAccessException
    SCHEDULE_UNCOMPLETED("SCHEDULE_004", HttpStatus.FORBIDDEN, "해당 일정이 완료되지 않았습니다."), // -> InvalidAccessException
    SCHEDULE_COMPLETED("SCHEDULE_005", HttpStatus.FORBIDDEN, "해당 일정이 이미 완료되었습니다."), // -> InvalidAccessException

    // ! 400 BAD_REQUEST
    CATEGORY_NOT_FOUND("SCHEDULE_006", HttpStatus.BAD_REQUEST, "일정 카테고리가 존재하지 않습니다."), // -> NotFoundCategoryException
    CATEGORY_MISMATCH("SCHEDULE_007", HttpStatus.BAD_REQUEST, "일정 카테고리를 변경할 수 없습니다."), // -> ScheduleCategoryMisMatchException
    SCHEDULE_OUT_OF_RANGE("SCHEDULE_008", HttpStatus.BAD_REQUEST, "일정의 날짜 값이 올바르지 않습니다."), // -> ScheduleOutOfRangeException

    // ! 404 NOT_FOUND
    SCHEDULE_NOT_FOUND("SCHEDULE_009", HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다."),
    SCHEDULE_MEMBER_NOT_FOUND("SCHEDULE_010", HttpStatus.NOT_FOUND, "일정에서 해당 사용자를 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
