package fullcare.backend.project;

public class CompletedProjectException extends RuntimeException {

    public CompletedProjectException() {
        super();
    }

    public CompletedProjectException(String message) {
        super(message);
    }

    public CompletedProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompletedProjectException(Throwable cause) {
        super(cause);
    }
}
