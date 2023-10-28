package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class SelfEvalException extends RestApiException {
    public SelfEvalException(ErrorCode errorCode) {
        super(errorCode);
    }
}
