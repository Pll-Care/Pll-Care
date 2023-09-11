package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class DuplicateEvalException extends RestApiException {


    public DuplicateEvalException(ErrorCode errorCode) {
        super(errorCode);
    }
}

