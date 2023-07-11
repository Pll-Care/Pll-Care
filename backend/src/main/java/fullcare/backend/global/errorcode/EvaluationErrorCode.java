package fullcare.backend.global.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EvaluationErrorCode implements ErrorCode {

    // ! 403 FORBIDDEN
    INVALID_ACCESS("EVAL_001", HttpStatus.FORBIDDEN, "해당 평가에 접근 권한이 없습니다."), // -> InvalidAccessException
    INVALID_DELETE("EVAL_002", HttpStatus.FORBIDDEN, "해당 평가에 대한 삭제 권한이 없습니다."), // -> InvalidAccessException
    INVALID_MODIFY("EVAL_003", HttpStatus.FORBIDDEN, "해당 평가에 대한 수정 권한이 없습니다."), // -> InvalidAccessException
    SELF_EVALUATION("EVAL_004", HttpStatus.FORBIDDEN, "자기 자신의 평가를 할 수 없습니다."), // -> SelfEvalException
    DUPLICATE_EVALUATION("EVAL_005", HttpStatus.FORBIDDEN, "동일한 평가를 다시 할 수 없습니다."), // -> DuplicateEvalException

    // ! 400 BAD_REQUEST
    SCORE_OUT_OF_RANGE("EVAL_006", HttpStatus.BAD_REQUEST, "최종 평가 점수가 범위를 벗어났습니다."), // -> EvalOutOfRangeException

    // ! 404 NOT_FOUND
    EVALUATION_NOT_FOUND("EVAL_007", HttpStatus.NOT_FOUND, "평가를 찾을 수 없습니다."); // -> EntityNotFoundException
    
    private final String code;
    private final HttpStatus status;
    private final String message;
}
