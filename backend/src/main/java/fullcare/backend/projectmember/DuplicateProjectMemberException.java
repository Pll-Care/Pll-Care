package fullcare.backend.projectmember;

public class DuplicateProjectMemberException extends RuntimeException {

    public DuplicateProjectMemberException() {
        super();
    }

    public DuplicateProjectMemberException(String message) {
        super(message);
    }

    public DuplicateProjectMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateProjectMemberException(Throwable cause) {
        super(cause);
    }
}
