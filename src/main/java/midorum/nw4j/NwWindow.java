package midorum.nw4j;

import java.awt.*;
import java.util.Optional;

public interface NwWindow {

    int getProcessId();

    NwProcess getProcess();

    boolean isExists();

    boolean isMaximized();

    boolean isMinimized();

    boolean isVisible();

    Rectangle getClientRectangle();

    Rectangle getClientToScreenRectangle();

    Rectangle getWindowRectangle();

    Optional<String> getTitle();

    Optional<String> getClassName();

    NwResult move(final int x, final int y);

    NwResult resize(final int width, final int height);

    NwResult moveAndResize(final int x, final int y, final int width, final int height);

    NwResult restore();

    NwResult maximize();

    NwResult minimize();

    NwResult close();

    NwResult bringToFront();

    NwResult setAlwaysOnTop(boolean alwaysOnTop);

    NwResult setTitle(String title);

    NwResult setKeyboardLayout(int keyboardLayoutId);

    //TODO enumerating window attributes
    //TODO enumerating window properties
}
