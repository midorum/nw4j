package midorum.nw4j.linux.x11;

import com.sun.jna.platform.unix.X11;
import midorum.nw4j.NwProcess;
import midorum.nw4j.NwResult;
import midorum.nw4j.NwWindow;
import midorum.nw4j.exception.NwRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Optional;

public class X11Window implements NwWindow {

    private final Logger logger = LoggerFactory.getLogger(X11Window.class);

    private final X11System.System system;
    private final X11.Window window;

    X11Window(X11System.System system, X11.Window window) {
        this.system = system;
        this.window = window;
        logger.trace("Created X11 window: ID={}", window);
    }

    @Override
    public int getProcessId() {
        try {
            return system.getIntProperty(window, X11.XA_CARDINAL, "_NET_WM_PID");
        } catch (NwX11Exception e) {
            throw new NwRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public NwProcess getProcess() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public boolean isExists() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public boolean isMaximized() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public boolean isMinimized() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public boolean isVisible() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public Rectangle getClientRectangle() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public Rectangle getClientToScreenRectangle() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public Rectangle getWindowRectangle() {
        final X11System.Geometry geometry = system.getGeometry(window);
        return new Rectangle(geometry.x(), geometry.y(), geometry.width(), geometry.height());
    }

    @Override
    public Optional<String> getTitle() {
        try {
            return Optional.ofNullable(system.getUtf8Property(window, system.getAtom("UTF8_STRING"), "_NET_WM_NAME"));
        } catch (NwX11Exception e) {
            try {
                return Optional.ofNullable(system.getUtf8Property(window, X11.XA_STRING, X11.XA_WM_NAME));
            } catch (NwX11Exception ex) {
                throw new NwRuntimeException(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public Optional<String> getClassName() {
        try {
            return Optional.ofNullable(system.getUtf8Property(window, X11.XA_STRING, X11.XA_WM_CLASS));
        } catch (NwX11Exception e) {
            throw new NwRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public NwResult move(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public NwResult resize(int width, int height) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public NwResult moveAndResize(int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public NwResult restore() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public NwResult maximize() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public NwResult minimize() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public NwResult close() {
        final int result = system.clientMsg(window, "_NET_CLOSE_WINDOW", 0, 0, 0, 0, 0);
        system.flush();
        return result == X11.Success ? NwResult.success : NwResult.error;
    }

    @Override
    public NwResult bringToFront() {
        final int result = system.clientMsg(window, "_NET_ACTIVE_WINDOW", 0, 0, 0, 0, 0);
        system.mapRaised(window);
        system.flush();
        return result == X11.Success ? NwResult.success : NwResult.error;
    }

    @Override
    public NwResult setAlwaysOnTop(boolean alwaysOnTop) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public NwResult setTitle(String title) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public NwResult setKeyboardLayout(int keyboardLayoutId) {
        throw new UnsupportedOperationException("Not supported yet");
    }
}
