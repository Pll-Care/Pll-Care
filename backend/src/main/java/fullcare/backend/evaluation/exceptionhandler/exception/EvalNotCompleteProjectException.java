package fullcare.backend.evaluation.exceptionhandler.exception;

public class EvalNotCompleteProjectException extends RuntimeException {
    public EvalNotCompleteProjectException(String message) {
        super(message);
    }

    public EvalNotCompleteProjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
