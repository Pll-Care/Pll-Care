package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EvaluationErrorCode implements ErrorCode {

    EVALUATION_NOT_FOUND("EVAL_001", HttpStatus.NOT_FOUND, "평가를 찾을 수 없습니다."),
    SCORE_OUT_OF_RANGE("EVAL_002", HttpStatus.BAD_REQUEST, "최종 평가 점수가 범위를 벗어났습니다."),
    UNAUTHORIZED_ACCESS("EVAL_003", HttpStatus.FORBIDDEN, "해당 평가에 접근 권한이 없습니다."),
    SELF_EVALUATION("EVAL_004", HttpStatus.FORBIDDEN, "자기 자신의 평가를 할 수 없습니다."),
    DUPLICATE_EVALUATION("EVAL_005", HttpStatus.FORBIDDEN, "동일한 평가를 다시 할 수 없습니다."),
    MID_EVAL_NOT_CREATE("EVAL_006", HttpStatus.FORBIDDEN, "완료된 일정만 평가할 수 있습니다.");


    private final String code;
    private final HttpStatus status;
    private final String message;
}
