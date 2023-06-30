package fullcare.backend.evaluation.exceptionhandler.exception;

public class NotCompletedProjectException extends RuntimeException {
    public NotCompletedProjectException(String message) {
        super(message);
    }

    public NotCompletedProjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
