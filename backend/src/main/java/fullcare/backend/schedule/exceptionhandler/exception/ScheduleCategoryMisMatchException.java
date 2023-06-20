package fullcare.backend.schedule.exceptionhandler.exception;

public class ScheduleCategoryMisMatchException extends RuntimeException{
    public ScheduleCategoryMisMatchException(String message) {
        super(message);
    }

    public ScheduleCategoryMisMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
