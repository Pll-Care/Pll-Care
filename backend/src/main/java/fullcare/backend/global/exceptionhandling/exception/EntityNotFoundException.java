package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class EntityNotFoundException extends RestApiException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
