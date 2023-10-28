package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class CompletedProjectException extends RestApiException {

    public CompletedProjectException(ErrorCode errorCode) {
        super(errorCode);
    }
}
