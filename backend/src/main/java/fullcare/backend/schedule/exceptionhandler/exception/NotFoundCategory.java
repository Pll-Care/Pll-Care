package fullcare.backend.schedule.exceptionhandler.exception;

public class NotFoundCategory  extends RuntimeException {
    public NotFoundCategory(String message) {
        super(message);
    }

    public NotFoundCategory(String message, Throwable cause) {
        super(message, cause);
    }
}

