package midorum.nw4j.exception;

public class NwRuntimeException extends RuntimeException {
    public NwRuntimeException(String message) {
        super(message);
    }

    public NwRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
