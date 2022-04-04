package midorum.nw4j.exception;

public class UnsupportedSystemException extends NwRuntimeException {
    public UnsupportedSystemException(String message) {
        super(message);
    }

    public UnsupportedSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
