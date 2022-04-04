package midorum.nw4j;

import com.sun.jna.platform.unix.X11;
import com.sun.jna.ptr.IntByReference;
import midorum.nw4j.exception.NwRuntimeException;
import midorum.nw4j.linux.x11.NwX11Exception;
import midorum.nw4j.linux.x11.X11Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class NwPlatformTest {

    final NwPlatform nwPlatform = new NwPlatform();

    @Test
    void getArchModel() {
        final String archModel = nwPlatform.getArchModel();
        assertNotNull(archModel);
        System.out.println(archModel);
    }

    @Test
    void getOsName() {
        final String osName = nwPlatform.getOsName();
        assertNotNull(osName);
        System.out.println(osName);
    }

    @Test
    void useSystem() {
        nwPlatform.useSystem(Assertions::assertNotNull);
    }

//    @Test
//    void testLinux() throws NwX11Exception {
//        final X11 x11 = X11.INSTANCE;
//        System.out.println("x11: " + x11);
//        final X11.Display x11Display = x11.XOpenDisplay(null);
//        if (x11Display == null) {
//            throw new Error("Can't open X Display");
//        }
//        System.out.println("x11Display: " + x11Display);
//        System.out.println();
//
//        final X11.Window rootWindow = x11.XDefaultRootWindow(x11Display);
//        System.out.println("rootWindow ID: " + String.format("0x%08X", getID(rootWindow)));
//        System.out.println(getGeometry(x11Display, rootWindow));
//        System.out.println();
//
//        final X11.Window activeWindow = X11Util.getWindowProperty(x11Display, rootWindow, X11.XA_WINDOW, "_NET_ACTIVE_WINDOW");
//        final int activeWindowId = getID(activeWindow);
//        System.out.println("active window ID: " + String.format("0x%08X", activeWindowId));
//        System.out.println();
//
//        final List<X11.Window> windowList = getAllWindows(x11Display, rootWindow);
//        windowList.forEach(window -> {
//            final int windowId = getID(window);
//            try {
//                System.out.println("window ID: " + String.format("0x%08X", windowId));
//                System.out.println(getGeometry(x11Display, window));
//                System.out.println("desktop: " + getDesktop(x11Display, window));
//                System.out.println("machine: " + getMachine(x11Display, window));
//                System.out.println("title: " + getTitle(x11Display, window));
//                System.out.println("window class: " + getWindowClass(x11Display, window));
//                System.out.println("PID: " + getPID(x11Display, window));
//                System.out.println("is active: " + (windowId == activeWindowId));
//                System.out.println();
//            } catch (NwX11Exception e) {
//                throw new NwRuntimeException(e.getMessage(), e);
//            }
//        });
//
//        x11.XCloseDisplay(x11Display);
//    }
//
//    private List<X11.Window> getAllWindows(X11.Display x11Display, X11.Window rootWindow) {
//        byte[] bytes;
//        try {
//            bytes = X11Util.getProperty(x11Display, rootWindow, X11.XA_WINDOW, "_NET_CLIENT_LIST");
//        } catch (NwX11Exception e) {
//            try {
//                bytes = X11Util.getProperty(x11Display, rootWindow, X11.XA_CARDINAL, "_WIN_CLIENT_LIST");
//            } catch (NwX11Exception e1) {
//                throw new NwRuntimeException("Cannot get client list properties (_NET_CLIENT_LIST or _WIN_CLIENT_LIST)");
//            }
//        }
//        final X11.Window[] windowList = new X11.Window[bytes.length / X11.Window.SIZE];
//        for (int i = 0; i < windowList.length; i++) {
//            windowList[i] = new X11.Window(X11Util.bytesToInt(bytes, X11.XID.SIZE * i));
//        }
//        return Arrays.stream(windowList).toList();
//    }
//
//    /**
//     * Returns the ID of the window.
//     *
//     * @return window ID
//     */
//    private int getID(final X11.Window x11Window) {
//        return x11Window.intValue();
//    }
//
//    /**
//     * Returns the title of the window.
//     *
//     * @return title of the window
//     * @throws X.X11Exception thrown if X11 window errors occurred
//     */
//    private String getTitle(final X11.Display x11Display, final X11.Window x11Window) throws NwX11Exception {
//        try {
//            return X11Util.getUtf8Property(x11Display, x11Window, X11Util.getAtom(x11Display, "UTF8_STRING"), "_NET_WM_NAME");
//        } catch (NwX11Exception e) {
//            return X11Util.getUtf8Property(x11Display, x11Window, X11.XA_STRING, X11.XA_WM_NAME);
//        }
//
//    }
//
//    /**
//     * Returns the window class.
//     *
//     * @return window class
//     * @throws X.X11Exception thrown if X11 window errors occurred
//     */
//    private String getWindowClass(final X11.Display x11Display, final X11.Window x11Window) throws NwX11Exception {
//        return X11Util.getUtf8Property(x11Display, x11Window, X11.XA_STRING, X11.XA_WM_CLASS);
//    }
//
//    /**
//     * Returns the PID of the window.
//     *
//     * @return PID of the window
//     * @throws X.X11Exception thrown if X11 window errors occurred
//     */
//    private Integer getPID(final X11.Display x11Display, final X11.Window x11Window) throws NwX11Exception {
//        return X11Util.getIntProperty(x11Display, x11Window, X11.XA_CARDINAL, "_NET_WM_PID");
//    }
//
//    /**
//     * Returns the desktop ID of the window.
//     *
//     * @return desktop ID of the window
//     * @throws X.X11Exception thrown if X11 window errors occurred
//     */
//    private int getDesktop(final X11.Display x11Display, final X11.Window x11Window) throws NwX11Exception {
//        try {
//            return X11Util.getIntProperty(x11Display, x11Window, X11.XA_CARDINAL, "_NET_WM_DESKTOP");
//        } catch (NwX11Exception e) {
//            return X11Util.getIntProperty(x11Display, x11Window, X11.XA_CARDINAL, "_WIN_WORKSPACE");
//        }
//    }
//
//    /**
//     * Returns the client machine name of the window.
//     *
//     * @return client machine name of the window
//     * @throws X.X11Exception thrown if X11 window errors occurred
//     */
//    private String getMachine(final X11.Display x11Display, final X11.Window x11Window) throws NwX11Exception {
//        return X11Util.getStringProperty(x11Display, x11Window, X11.XA_STRING, "WM_CLIENT_MACHINE");
//    }
//
//    /**
//     * Returns the geometry of the window.
//     *
//     * @return geometry of the window
//     */
//    private Geometry getGeometry(final X11.Display x11Display, final X11.Window x11Window) {
//        X11.WindowByReference junkRoot = new X11.WindowByReference();
//        IntByReference junkX = new IntByReference();
//        IntByReference junkY = new IntByReference();
//        IntByReference x = new IntByReference();
//        IntByReference y = new IntByReference();
//        IntByReference width = new IntByReference();
//        IntByReference height = new IntByReference();
//        IntByReference borderWidth = new IntByReference();
//        IntByReference depth = new IntByReference();
//
//        X11.INSTANCE.XGetGeometry(x11Display, x11Window, junkRoot, junkX, junkY, width, height, borderWidth, depth);
//
//        X11.INSTANCE.XTranslateCoordinates(x11Display, x11Window, junkRoot.getValue(), junkX.getValue(),
//                junkY.getValue(), x, y, junkRoot);
//
//        return new Geometry(x.getValue(), y.getValue(), width.getValue(), height.getValue(),
//                borderWidth.getValue(), depth.getValue());
//    }
//
//    record Geometry(int x, int y, int width, int height, int borderWidth, int depth) {
//    }
}