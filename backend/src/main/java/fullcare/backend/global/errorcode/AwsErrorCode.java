package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum AwsErrorCode implements ErrorCode{
    NOT_FOUND_FILE("AWS_001", HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    UPLOAD_FAIL("AWS_002", HttpStatus.BAD_REQUEST, "파일 업로드를 실패했습니다.");
    private final String code;
    private final HttpStatus status;
    private final String message;
}
