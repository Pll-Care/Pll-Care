package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;

public class UploadException extends RestApiException {
    public UploadException(ErrorCode errorCode) {
        super(errorCode);
    }
}
