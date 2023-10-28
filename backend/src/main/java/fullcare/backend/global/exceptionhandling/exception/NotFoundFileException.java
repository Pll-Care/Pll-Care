package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class NotFoundFileException extends RestApiException {
    public NotFoundFileException(ErrorCode errorCode) {
        super(errorCode);
    }
}