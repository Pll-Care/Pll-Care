package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class NotFoundCategoryException extends RestApiException {
    public NotFoundCategoryException(ErrorCode errorCode) {
        super(errorCode);
    }
}

