package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class InvalidApplyException extends RestApiException {

    public InvalidApplyException(ErrorCode errorCode) {
        super(errorCode);
    }
}

