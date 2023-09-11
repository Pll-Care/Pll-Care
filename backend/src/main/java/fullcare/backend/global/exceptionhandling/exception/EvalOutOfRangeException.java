package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class EvalOutOfRangeException extends RestApiException {
    public EvalOutOfRangeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
