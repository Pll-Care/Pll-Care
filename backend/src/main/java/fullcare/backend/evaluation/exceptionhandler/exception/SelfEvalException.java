package fullcare.backend.evaluation.exceptionhandler.exception;

public class SelfEvalException extends RuntimeException {
    public SelfEvalException(String message) {
        super(message);
    }

    public SelfEvalException(String message, Throwable cause) {
        super(message, cause);
    }
}
