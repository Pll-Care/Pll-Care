package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    // ! 403 Forbidden
    MEMBER_PROFILE_INVALID_ACCESS("MEMBER_001", HttpStatus.FORBIDDEN, "해당 사용자 프로필에 접근 권한이 없습니다."), // -> InvalidAccessException
    MEMBER_PROFILE_INVALID_DELETE("MEMBER_002", HttpStatus.FORBIDDEN, "해당 사용자 프로필에 대한 삭제 권한이 없습니다."), // -> InvalidAccessException
    MEMBER_PROFILE_INVALID_MODIFY("MEMBER_003", HttpStatus.FORBIDDEN, "해당 사용자 프로필에 대한 수정 권한이 없습니다."), // -> InvalidAccessException

    // ! 404 NOT_FOUND
    MEMBER_NOT_FOUND("MEMBER_004", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."); // -> EntityNotFoundException

    private final String code;
    private final HttpStatus status;
    private final String message;
}
