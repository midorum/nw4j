package midorum.nw4j.linux.x11;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;
import midorum.nw4j.NwProcess;
import midorum.nw4j.NwSystem;
import midorum.nw4j.NwThread;
import midorum.nw4j.NwWindow;
import midorum.nw4j.exception.NwLastErrorException;
import midorum.nw4j.exception.NwRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Some code segments in this class are based on the code of <a href="https://github.com/java-native-access/jna/blob/master/contrib/x11/src/jnacontrib/x11/api/X.java">Object oriented X window system</a>
 * written by Stefan Endrullis.
 * Thanks a lot, Stefan!
 */
public class X11System implements NwSystem {

    private final Logger logger = LoggerFactory.getLogger(X11System.class);

    private final X11 x11;
    private final X11.Display x11Display;
    private final X11.Window rootWindow;
    private final X11System.System system;

    public X11System() {
        x11 = X11.INSTANCE;
        x11Display = x11.XOpenDisplay(null);
        if (x11Display == null) {
            throw new NwLastErrorException("Can't open X Display");
        }
        rootWindow = x11.XDefaultRootWindow(x11Display);
        this.system = new System();
    }

    X11 getX11() {
        return x11;
    }

    X11.Display getX11Display() {
        return x11Display;
    }

    X11.Window getRootWindow() {
        return rootWindow;
    }

    @Override
    public List<NwWindow> listWindows() {
        return Arrays.stream(getAllWindows())
                .map(window -> (NwWindow) new X11Window(system, window))
                .toList();
    }

    @Override
    public List<NwProcess> listProcesses() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public List<NwThread> listThreads() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public int getNumberOfProcessors() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public long getSystemTime() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public Rectangle getScreenResolution() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public Optional<NwWindow> getFocusedWindow() {
        try {
            return Optional.ofNullable(system.getWindowProperty(rootWindow, X11.XA_WINDOW, "_NET_ACTIVE_WINDOW"))
                    .map(window -> new X11Window(system, window));
        } catch (NwX11Exception e) {
            throw new NwRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws Exception {
        if (x11Display != null) x11.XCloseDisplay(x11Display);
    }

    private X11.Window[] getAllWindows() {
        byte[] bytes;
        try {
            bytes = system.getProperty(rootWindow, X11.XA_WINDOW, "_NET_CLIENT_LIST");
        } catch (NwX11Exception e) {
            try {
                bytes = system.getProperty(rootWindow, X11.XA_CARDINAL, "_WIN_CLIENT_LIST");
            } catch (NwX11Exception e1) {
                throw new NwRuntimeException("Cannot get client list properties (_NET_CLIENT_LIST or _WIN_CLIENT_LIST)");
            }
        }
        final X11.Window[] windowList = new X11.Window[bytes.length / X11.Window.SIZE];
        for (int i = 0; i < windowList.length; i++) {
            windowList[i] = new X11.Window(system.bytesToInt(bytes, X11.XID.SIZE * i));
        }
        return windowList;
    }

    class System {

        public int clientMsg(final X11.Window x11Window, String msg, int data0, int data1, int data2, int data3, int data4) {
            return clientMsg(
                    x11Window,
                    msg,
                    new NativeLong(data0),
                    new NativeLong(data1),
                    new NativeLong(data2),
                    new NativeLong(data3),
                    new NativeLong(data4)
            );
        }

        public int clientMsg(final X11.Window x11Window, String msg, NativeLong data0, NativeLong data1, NativeLong data2, NativeLong data3, NativeLong data4) {
            X11.XClientMessageEvent event;
            NativeLong mask = new NativeLong(X11.SubstructureRedirectMask | X11.SubstructureNotifyMask);

            event = new X11.XClientMessageEvent();
            event.type = X11.ClientMessage;
            event.serial = new NativeLong(0);
            event.send_event = 1;
            event.message_type = getAtom(msg);
            event.window = x11Window;
            event.format = 32;
            event.data.setType(NativeLong[].class);
            event.data.l[0] = data0;
            event.data.l[1] = data1;
            event.data.l[2] = data2;
            event.data.l[3] = data3;
            event.data.l[4] = data4;

            X11.XEvent e = new X11.XEvent();
            e.setTypedValue(event);

            if (x11.XSendEvent(x11Display, rootWindow, 0, mask, e) != 0) {
                return X11.Success;
            } else {
                throw new NwRuntimeException("Cannot send " + msg + " event.");
            }
        }

        public int mapRaised(final X11.Window x11Window) {
            return x11.XMapRaised(x11Display, x11Window);
        }

        public void flush() {
            x11.XFlush(x11Display);
        }

        public int bytesToInt(byte[] prop) {
            return ((prop[3] & 0xff) << 24)
                    | ((prop[2] & 0xff) << 16)
                    | ((prop[1] & 0xff) << 8)
                    | ((prop[0] & 0xff));
        }

        public int bytesToInt(byte[] prop, int offset) {
            return ((prop[3 + offset] & 0xff) << 24)
                    | ((prop[2 + offset] & 0xff) << 16)
                    | ((prop[1 + offset] & 0xff) << 8)
                    | ((prop[offset] & 0xff));
        }

        /**
         * Get internal atoms by name.
         *
         * @param name name of the atom
         * @return atom
         */
        public X11.Atom getAtom(String name) {
            return X11.INSTANCE.XInternAtom(x11Display, name, false);
        }

        /**
         * Returns the property value as a byte array.
         *
         * @param xa_prop_type property type
         * @param xa_prop_name property name
         * @return property value as a byte array
         * @throws NwX11Exception thrown if X11 window errors occurred
         */
        public byte[] getProperty(final X11.Window x11Window, X11.Atom xa_prop_type, String xa_prop_name) throws NwX11Exception {
            return getProperty(x11Window, xa_prop_type, getAtom(xa_prop_name));
        }

        /**
         * Maximal property value length.
         */
        public static final int MAX_PROPERTY_VALUE_LEN = 4096;

        /**
         * Returns the property value as a byte array.
         *
         * @param xa_prop_type property type
         * @param xa_prop_name property name
         * @return property value as a byte array
         * @throws NwX11Exception thrown if X11 window errors occurred
         */
        public byte[] getProperty(final X11.Window x11Window, X11.Atom xa_prop_type, X11.Atom xa_prop_name) throws NwX11Exception {
            X11.AtomByReference xa_ret_type_ref = new X11.AtomByReference();
            IntByReference ret_format_ref = new IntByReference();
            NativeLongByReference ret_nitems_ref = new NativeLongByReference();
            NativeLongByReference ret_bytes_after_ref = new NativeLongByReference();
            PointerByReference ret_prop_ref = new PointerByReference();

            NativeLong long_offset = new NativeLong(0);
            NativeLong long_length = new NativeLong(MAX_PROPERTY_VALUE_LEN / 4);

            /* MAX_PROPERTY_VALUE_LEN / 4 explanation (XGetWindowProperty manpage):
             *
             * long_length = Specifies the length in 32-bit multiples of the
             *               data to be retrieved.
             */
            if (X11.INSTANCE.XGetWindowProperty(x11Display, x11Window, xa_prop_name, long_offset, long_length, false,
                    xa_prop_type, xa_ret_type_ref, ret_format_ref,
                    ret_nitems_ref, ret_bytes_after_ref, ret_prop_ref) != X11.Success) {
                String prop_name = X11.INSTANCE.XGetAtomName(x11Display, xa_prop_name);
                throw new NwX11Exception("Cannot get " + prop_name + " property.");
            }

            X11.Atom xa_ret_type = xa_ret_type_ref.getValue();
            Pointer ret_prop = ret_prop_ref.getValue();
            if (xa_ret_type == null || xa_prop_type == null ||
                    !xa_ret_type.toNative().equals(xa_prop_type.toNative())) {
                //the specified property does not exist for the specified window
                X11.INSTANCE.XFree(ret_prop);
                String prop_name = X11.INSTANCE.XGetAtomName(x11Display, xa_prop_name);
                throw new NwX11Exception("Invalid type of " + prop_name + " property");
            }

            int ret_format = ret_format_ref.getValue();
            long ret_nitems = ret_nitems_ref.getValue().longValue();

            // null terminate the result to make string handling easier
            int nbytes;
            if (ret_format == 32)
                nbytes = Native.LONG_SIZE;
            else if (ret_format == 16)
                nbytes = Native.LONG_SIZE / 2;
            else if (ret_format == 8)
                nbytes = 1;
            else if (ret_format == 0)
                nbytes = 0;
            else
                throw new NwX11Exception("Invalid return format");
            int length = Math.min((int) ret_nitems * nbytes, MAX_PROPERTY_VALUE_LEN);

            byte[] ret = ret_prop.getByteArray(0, length);

            X11.INSTANCE.XFree(ret_prop);
            return ret;
        }

        /**
         * Returns the property value as window.
         *
         * @param xa_prop_type property type
         * @param xa_prop_name property name
         * @return property value as window, or null if not found
         * @throws NwX11Exception thrown if X11 window errors occurred
         */
        public X11.Window getWindowProperty(final X11.Window x11Window, X11.Atom xa_prop_type, X11.Atom xa_prop_name) throws NwX11Exception {
            Integer windowId = getIntProperty(x11Window, xa_prop_type, xa_prop_name);
            if (windowId == null) {
                return null;
            }
            return new X11.Window(windowId);
        }

        /**
         * Returns the property value as window.
         *
         * @param xa_prop_type property type
         * @param xa_prop_name property name
         * @return property value as window
         * @throws NwX11Exception thrown if X11 window errors occurred
         */
        public X11.Window getWindowProperty(final X11.Window x11Window, X11.Atom xa_prop_type, String xa_prop_name) throws NwX11Exception {
            return getWindowProperty(x11Window, xa_prop_type, getAtom(xa_prop_name));
        }

        /**
         * Returns the property value as UTF8 string where every '\0' character is replaced by '.'.
         *
         * @param xa_prop_type property type
         * @param xa_prop_name property name
         * @return property value as UTF8 string where every '\0' character is replaced by '.'
         * @throws NwX11Exception thrown if X11 window errors occurred
         */
        public String getUtf8Property(final X11.Window x11Window, X11.Atom xa_prop_type, X11.Atom xa_prop_name) throws NwX11Exception {
            try {
                byte[] property = getNullReplacedStringProperty(x11Window, xa_prop_type, xa_prop_name);
                if (property == null) {
                    return null;
                }
                return new String(property, "UTF8");
            } catch (UnsupportedEncodingException e) {
                throw new NwX11Exception(e.getMessage(), e);
            }
        }

        /**
         * Returns the property value as UTF8 string where every '\0' character is replaced by '.'.
         *
         * @param xa_prop_type property type
         * @param xa_prop_name property name
         * @return property value as UTF8 string where every '\0' character is replaced by '.'
         * @throws NwX11Exception thrown if X11 window errors occurred
         */
        public String getUtf8Property(final X11.Window x11Window, X11.Atom xa_prop_type, String xa_prop_name) throws NwX11Exception {
            return getUtf8Property(x11Window, xa_prop_type, getAtom(xa_prop_name));
        }

        /**
         * Returns the property value as integer.
         *
         * @param xa_prop_type property type
         * @param xa_prop_name property name
         * @return property value as integer or null if not found
         * @throws NwX11Exception thrown if X11 window errors occurred
         */
        public Integer getIntProperty(final X11.Window x11Window, X11.Atom xa_prop_type, X11.Atom xa_prop_name) throws NwX11Exception {
            byte[] property = getProperty(x11Window, xa_prop_type, xa_prop_name);
            if (property == null) {
                return null;
            }
            return bytesToInt(property);
        }

        /**
         * Returns the property value as integer.
         *
         * @param xa_prop_type property type
         * @param xa_prop_name property name
         * @return property value as integer
         * @throws NwX11Exception thrown if X11 window errors occurred
         */
        public Integer getIntProperty(final X11.Window x11Window, X11.Atom xa_prop_type, String xa_prop_name) throws NwX11Exception {
            return getIntProperty(x11Window, xa_prop_type, getAtom(xa_prop_name));
        }

        /**
         * Returns the property value as string where every '\0' character is replaced by '.'.
         *
         * @param xa_prop_type property type
         * @param xa_prop_name property name
         * @return property value as string where every '\0' character is replaced by '.'
         * @throws NwX11Exception thrown if X11 window errors occurred
         */
        public String getStringProperty(final X11.Window x11Window, X11.Atom xa_prop_type, String xa_prop_name) throws NwX11Exception {
            return new String(getNullReplacedStringProperty(x11Window, xa_prop_type, xa_prop_name));
        }

        /**
         * Returns the property value as byte array where every '\0' character is replaced by '.'.
         *
         * @param xa_prop_type property type
         * @param xa_prop_name property name
         * @return property value as byte array where every '\0' character is replaced by '.'
         * @throws NwX11Exception thrown if X11 window errors occurred
         */
        public byte[] getNullReplacedStringProperty(final X11.Window x11Window, X11.Atom xa_prop_type, String xa_prop_name) throws NwX11Exception {
            return getNullReplacedStringProperty(x11Window, xa_prop_type, getAtom(xa_prop_name));
        }

        /**
         * Returns the property value as byte array where every '\0' character is replaced by '.'.
         *
         * @param xa_prop_type property type
         * @param xa_prop_name property name
         * @return property value as byte array where every '\0' character is replaced by '.'.  null if the property was not found
         * @throws NwX11Exception thrown if X11 window errors occurred
         */
        public byte[] getNullReplacedStringProperty(final X11.Window x11Window, X11.Atom xa_prop_type, X11.Atom xa_prop_name) throws NwX11Exception {
            byte[] bytes = getProperty(x11Window, xa_prop_type, xa_prop_name);

            if (bytes == null) {
                return null;
            }

            // search for '\0'
            int i;
            for (i = 0; i < bytes.length; i++) {
                if (bytes[i] == '\0') {
                    bytes[i] = '.';
                }
            }

            return bytes;
        }

        /**
         * Returns the XWindowAttributes of the window.
         *
         * @return XWindowAttributes of the window
         */
        public X11.XWindowAttributes getXWindowAttributes(final X11.Window x11Window) {
            X11.XWindowAttributes xwa = new X11.XWindowAttributes();
            X11.INSTANCE.XGetWindowAttributes(x11Display, x11Window, xwa);

            return xwa;
        }

        /**
         * Returns the geometry of the window.
         *
         * @return geometry of the window
         */
        public Geometry getGeometry(final X11.Window x11Window) {
            X11.WindowByReference junkRoot = new X11.WindowByReference();
            IntByReference junkX = new IntByReference();
            IntByReference junkY = new IntByReference();
            IntByReference x = new IntByReference();
            IntByReference y = new IntByReference();
            IntByReference width = new IntByReference();
            IntByReference height = new IntByReference();
            IntByReference borderWidth = new IntByReference();
            IntByReference depth = new IntByReference();

            X11.INSTANCE.XGetGeometry(x11Display, x11Window, junkRoot, junkX, junkY, width, height, borderWidth, depth);
            logger.trace("\nXGetGeometry:\njunkX={}, junkY={},\nwidth={}, height={}",
                    junkX, junkY, width, height);

            X11.INSTANCE.XTranslateCoordinates(x11Display, x11Window, junkRoot.getValue(), junkX.getValue(),
                    junkY.getValue(), x, y, junkRoot);
            logger.trace("\nXTranslateCoordinates:\njunkX={}, junkY={},\nx={}, y={}",
                    junkX, junkY, x, y);

            return new Geometry(x.getValue(), y.getValue(), width.getValue(), height.getValue(),
                    borderWidth.getValue(), depth.getValue());
        }

    }

    record Geometry(int x, int y, int width, int height, int borderWidth, int depth) {
    }
}
/*

83 [main] TRACE midorum.nw4j.linux.x11.X11System -
XGetGeometry:
junkX=int@0x7fb7d06ce6e0=0x0 (0), junkY=int@0x7fb7d06ce700=0x18 (24),
width=int@0x7fb7d06caa30=0x780 (1920), height=int@0x7fb7d06caa50=0x40b (1035)
83 [main] TRACE midorum.nw4j.linux.x11.X11System -
XTranslateCoordinates:
junkX=int@0x7fb7d06ce6e0=0x0 (0), junkY=int@0x7fb7d06ce700=0x18 (24),
x=int@0x7fb7d06ca9f0=0x0 (0), y=int@0x7fb7d06caa10=0x30 (48)
java.awt.Rectangle[x=0,y=48,width=1920,height=1035]

----------------

77 [main] TRACE midorum.nw4j.linux.x11.X11System -
XGetGeometry:
junkX=int@0x7efcd06a4e90=0x1 (1), junkY=int@0x7efcd06a1180=0x18 (24),
width=int@0x7efcd06a11e0=0x26e (622), height=int@0x7efcd06a1200=0x1f8 (504)
77 [main] TRACE midorum.nw4j.linux.x11.X11System -
XTranslateCoordinates:
junkX=int@0x7efcd06a4e90=0x1 (1), junkY=int@0x7efcd06a1180=0x18 (24),
x=int@0x7efcd06a11a0=0x2aa (682), y=int@0x7efcd06a11c0=0x138 (312)
java.awt.Rectangle[x=682,y=312,width=622,height=504]

-----------------

75 [main] TRACE midorum.nw4j.linux.x11.X11System -
XGetGeometry:
junkX=int@0x7ff974679570=0x1 (1), junkY=int@0x7ff974679590=0x18 (24),
width=int@0x7ff9746795f0=0x26e (622), height=int@0x7ff974679610=0x1f8 (504)
75 [main] TRACE midorum.nw4j.linux.x11.X11System -
XTranslateCoordinates:
junkX=int@0x7ff974679570=0x1 (1), junkY=int@0x7ff974679590=0x18 (24),
x=int@0x7ff9746795b0=0x2 (2), y=int@0x7ff9746795d0=0x30 (48)
java.awt.Rectangle[x=2,y=48,width=622,height=504]

 */