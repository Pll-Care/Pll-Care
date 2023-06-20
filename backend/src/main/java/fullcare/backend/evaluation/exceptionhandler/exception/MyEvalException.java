package fullcare.backend.evaluation.exceptionhandler.exception;

public class MyEvalException  extends RuntimeException {
    public MyEvalException(String message) {
        super(message);
    }

    public MyEvalException(String message, Throwable cause) {
        super(message, cause);
    }
}
