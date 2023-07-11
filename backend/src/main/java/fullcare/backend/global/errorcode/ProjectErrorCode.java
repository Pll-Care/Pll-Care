package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProjectErrorCode implements ErrorCode {

    // ! 403 Forbidden
    INVALID_ACCESS("PROJECT_001", HttpStatus.FORBIDDEN, "해당 프로젝트에 접근 권한이 없습니다."), // -> InvalidAccessException
    INVALID_DELETE("PROJECT_002", HttpStatus.FORBIDDEN, "해당 프로젝트에 대한 삭제 권한이 없습니다."), // -> InvalidAccessException
    INVALID_MODIFY("PROJECT_003", HttpStatus.FORBIDDEN, "해당 프로젝트에 대한 수정 권한이 없습니다."), // -> InvalidAccessException
    DUPLICATE_PROJECT_MEMBER("PROJECT_004", HttpStatus.FORBIDDEN, "이미 프로젝트에 소속되어 있습니다."), // -> DuplicateProjectMemberException
    PROJECT_COMPLETED("PROJECT_005", HttpStatus.FORBIDDEN, "이미 완료된 프로젝트입니다."), // -> NotCompletedProjectException
    PROJECT_UNCOMPLETED("PROJECT_006", HttpStatus.FORBIDDEN, "완료되지 않은 프로젝트입니다."), // -> CompletedProjectException

    // ! 404 NOT_FOUND
    PROJECT_NOT_FOUND("PROJECT_007", HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."),
    PROJECT_MEMBER_NOT_FOUND("PROJECT_008", HttpStatus.NOT_FOUND, "프로젝트에서 해당 사용자를 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
