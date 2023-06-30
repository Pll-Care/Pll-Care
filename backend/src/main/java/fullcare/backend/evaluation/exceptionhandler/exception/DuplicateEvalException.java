package fullcare.backend.evaluation.exceptionhandler.exception;

public class DuplicateEvalException extends RuntimeException {
    public DuplicateEvalException(String message) {
        super(message);
    }

    public DuplicateEvalException(String message, Throwable cause) {
        super(message, cause);
    }
}

