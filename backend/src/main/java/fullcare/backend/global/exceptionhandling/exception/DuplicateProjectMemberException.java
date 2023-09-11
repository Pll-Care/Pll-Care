package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class DuplicateProjectMemberException extends RestApiException {
    public DuplicateProjectMemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
