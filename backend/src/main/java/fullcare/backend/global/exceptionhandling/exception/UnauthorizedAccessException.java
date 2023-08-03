package fullcare.backend.global.exceptionhandling.exception;


import fullcare.backend.global.errorcode.ErrorCode;

public class UnauthorizedAccessException extends RestApiException {
    public UnauthorizedAccessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
