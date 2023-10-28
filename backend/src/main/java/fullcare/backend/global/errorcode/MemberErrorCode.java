package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    MEMBER_NOT_FOUND("MEMBER_001", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    MEMBER_PROFILE_UNAUTHORIZED_ACCESS("MEMBER_002", HttpStatus.FORBIDDEN, "해당 사용자 프로필에 접근 권한이 없습니다."),
    MEMBER_PROFILE_UNAUTHORIZED_MODIFY("MEMBER_003", HttpStatus.FORBIDDEN, "해당 사용자 프로필에 대한 수정 권한이 없습니다."), // ? unused
    MEMBER_PROFILE_UNAUTHORIZED_DELETE("MEMBER_004", HttpStatus.FORBIDDEN, "해당 사용자 프로필에 대한 삭제 권한이 없습니다."); // ? unused


    private final String code;
    private final HttpStatus status;
    private final String message;
}
