package midorum.nw4j.win32;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import midorum.nw4j.exception.NwLastErrorException;
import org.slf4j.Logger;

import java.util.Optional;

public final class Win32Util {

    private Win32Util() {
    }

    public static String getErrorMessage(final int errorCode) {
        return getErrorMessage(errorCode, null);
    }

    public static String getErrorMessage(final int errorCode, final Object thrower) {
        return Kernel32Util.formatMessageFromLastErrorCode(errorCode)
                + "(" + errorCode + ")"
                + Optional.ofNullable(thrower).map(o -> " [thrower: " + thrower + "]").orElse("");
    }

    public static String getLastErrorMessage() {
        final int lastErrorCode = Kernel32.INSTANCE.GetLastError();
        return getErrorMessage(lastErrorCode);
    }

    public static NwLastErrorException getLastErrorException() {
        return getLastErrorException(null);
    }

    public static NwLastErrorException getLastErrorException(final Object thrower) {
        final int lastErrorCode = Kernel32.INSTANCE.GetLastError();
        return new NwLastErrorException(lastErrorCode, getErrorMessage(lastErrorCode, thrower));
    }

    public static void traceStack(Logger logger, String marker) {
        if (logger.isTraceEnabled()) {
            Exception exception = new Exception(marker);
            logger.trace(exception.getMessage(), exception);
        }
    }

    public static void traceStack(Logger logger) {
        traceStack(logger, "You asked to be notified when...");
    }

    public static void traceLastResult(Logger logger, String marker) {
        if (logger.isTraceEnabled()) {
            logger.trace("last result: {} -> {}", marker, getLastErrorMessage());
        }
    }

    public static Optional<String> trimToOptional(String string) {
        return Optional.ofNullable(string).map(String::strip).filter(s -> !s.isBlank());
    }
}
