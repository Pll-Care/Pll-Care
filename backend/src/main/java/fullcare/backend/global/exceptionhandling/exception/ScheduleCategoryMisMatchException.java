package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class ScheduleCategoryMisMatchException extends RestApiException {
    public ScheduleCategoryMisMatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
