package fullcare.backend.schedule.exceptionhandler.exception;

public class ScheduleOutOfRangeException extends RuntimeException {
    public ScheduleOutOfRangeException(String message) {
        super(message);
    }

    public ScheduleOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
