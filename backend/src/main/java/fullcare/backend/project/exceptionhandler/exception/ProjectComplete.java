package fullcare.backend.project.exceptionhandler.exception;

public class ProjectComplete extends RuntimeException {
    public ProjectComplete(String message) {
        super(message);
    }

    public ProjectComplete(String message, Throwable cause) {
        super(message, cause);
    }
}

