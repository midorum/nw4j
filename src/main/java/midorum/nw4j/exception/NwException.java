package midorum.nw4j.exception;

public class NwException extends Exception {
    public NwException(String message) {
        super(message);
    }

    public NwException(String message, Throwable cause) {
        super(message, cause);
    }
}
