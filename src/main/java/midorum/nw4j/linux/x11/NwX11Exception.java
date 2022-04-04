package midorum.nw4j.linux.x11;

import midorum.nw4j.exception.NwException;

public class NwX11Exception extends NwException {
    public NwX11Exception(String message) {
        super(message);
    }

    public NwX11Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
