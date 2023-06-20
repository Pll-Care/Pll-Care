package fullcare.backend.evaluation.exceptionhandler.exception;

public class EvalOutOfRangeException  extends RuntimeException {
    public EvalOutOfRangeException(String message) {
        super(message);
    }

    public EvalOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
