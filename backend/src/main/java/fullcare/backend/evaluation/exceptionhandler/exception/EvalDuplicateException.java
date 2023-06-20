package fullcare.backend.evaluation.exceptionhandler.exception;

public class EvalDuplicateException  extends RuntimeException {
    public EvalDuplicateException(String message) {
        super(message);
    }

    public EvalDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}

