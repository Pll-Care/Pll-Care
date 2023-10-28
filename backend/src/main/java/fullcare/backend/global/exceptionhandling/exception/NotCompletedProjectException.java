package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class NotCompletedProjectException extends RestApiException {
    public NotCompletedProjectException(ErrorCode errorCode) {
        super(errorCode);
    }
}
