package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProjectErrorCode implements ErrorCode {

    PROJECT_NOT_FOUND("PROJECT_001", HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."),
    PROJECT_MEMBER_NOT_FOUND("PROJECT_002", HttpStatus.NOT_FOUND, "프로젝트에서 해당 사용자를 찾을 수 없습니다."),

    INVALID_DATE_RANGE("PROJECT_003", HttpStatus.BAD_REQUEST, "시작일자와 종료일자가 올바르지 않습니다."),

    UNAUTHORIZED_ACCESS("PROJECT_004", HttpStatus.FORBIDDEN, "해당 프로젝트에 대한 접근 권한이 없습니다."),
    UNAUTHORIZED_MODIFY("PROJECT_005", HttpStatus.FORBIDDEN, "해당 프로젝트에 대한 수정 권한이 없습니다."),
    UNAUTHORIZED_DELETE("PROJECT_006", HttpStatus.FORBIDDEN, "해당 프로젝트에 대한 삭제 권한이 없습니다."),
    UNAUTHORIZED_COMPLETE("PROJECT_007", HttpStatus.FORBIDDEN, "해당 프로젝트에 대한 완료 권한이 없습니다."),
    UNAUTHORIZED_ACTION("PROJECT_008", HttpStatus.FORBIDDEN, "해당 프로젝트의 해당 기능에 대한 권한이 없습니다."),

    INVALID_MODIFY("PROJECT_009", HttpStatus.FORBIDDEN, "완료된 프로젝트는 수정할 수 없습니다."),
    INVALID_DELETE("PROJECT_010", HttpStatus.FORBIDDEN, "완료된 프로젝트는 삭제할 수 없습니다."),
    INVALID_ACTION("PROJECT_011", HttpStatus.FORBIDDEN, "완료된 프로젝트에서는 해당 기능을 사용할 수 없습니다."),
    LEADER_INVALID_ACTION("PROJECT_012", HttpStatus.FORBIDDEN, "프로젝트 리더는 사용할 수 없는 기능입니다."),


    DUPLICATE_PROJECT_MEMBER("PROJECT_013", HttpStatus.FORBIDDEN, "이미 프로젝트에 소속되어 있습니다."),
    PROJECT_COMPLETED("PROJECT_014", HttpStatus.FORBIDDEN, "이미 완료된 프로젝트입니다."),
    PROJECT_UNCOMPLETED("PROJECT_015", HttpStatus.FORBIDDEN, "완료되지 않은 프로젝트입니다.");


    private final String code;
    private final HttpStatus status;
    private final String message;
}
