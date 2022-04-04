package midorum.nw4j.exception;

public class UnsupportedPlatformException extends NwRuntimeException {
    public UnsupportedPlatformException(String message) {
        super(message);
    }

    public UnsupportedPlatformException(String message, Throwable cause) {
        super(message, cause);
    }
}
