package fullcare.backend.global.exceptionhandling.exception;

import fullcare.backend.global.errorcode.ErrorCode;


public class ScheduleOutOfRangeException extends RestApiException {
    public ScheduleOutOfRangeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
