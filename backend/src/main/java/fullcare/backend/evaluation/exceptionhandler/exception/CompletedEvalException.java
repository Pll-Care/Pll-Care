package fullcare.backend.evaluation.exceptionhandler.exception;

public class CompletedEvalException extends RuntimeException {
    public CompletedEvalException(String message) {
        super(message);
    }

    public CompletedEvalException(String message, Throwable cause) {
        super(message, cause);
    }
}
