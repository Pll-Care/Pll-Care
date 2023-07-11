package fullcare.backend.global.exceptionhandling.exception;


import fullcare.backend.global.errorcode.ErrorCode;

public class InvalidAccessException extends RestApiException {
    public InvalidAccessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
