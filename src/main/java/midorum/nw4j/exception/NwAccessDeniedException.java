package midorum.nw4j.exception;

public class NwAccessDeniedException extends NwRuntimeException {
    public NwAccessDeniedException(String message) {
        super(message);
    }

    public NwAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
