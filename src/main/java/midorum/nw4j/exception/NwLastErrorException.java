package midorum.nw4j.exception;

import com.sun.jna.LastErrorException;

public class NwLastErrorException extends LastErrorException {
    public NwLastErrorException(String msg) {
        super(msg);
    }

    public NwLastErrorException(int code) {
        super(code);
    }

    public NwLastErrorException(int code, String msg) {
        super(code, msg);
    }
}
