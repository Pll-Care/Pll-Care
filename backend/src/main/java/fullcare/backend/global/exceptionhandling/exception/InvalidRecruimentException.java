package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class InvalidRecruimentException extends RestApiException {

    public InvalidRecruimentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
