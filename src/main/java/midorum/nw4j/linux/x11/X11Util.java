package midorum.nw4j.linux.x11;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;

import java.io.UnsupportedEncodingException;

/**
 * Some code segments in this class are based on the code of <a href="https://github.com/java-native-access/jna/blob/master/contrib/x11/src/jnacontrib/x11/api/X.java">Object oriented X window system</a>
 * written by Stefan Endrullis.
 * Thanks a lot, Stefan!
 */
public final class X11Util {

    private X11Util() {
    }
}
