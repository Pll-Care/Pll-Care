package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;


public class InvalidDateRangeException extends RestApiException {
    public InvalidDateRangeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
