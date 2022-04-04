package midorum.nw4j.linux;

import midorum.nw4j.NwSystem;
import midorum.nw4j.exception.UnsupportedSystemException;
import midorum.nw4j.linux.x11.X11System;

public class LinuxSystemFactory {

    private LinuxSystemFactory() {
    }

    public static NwSystem getSystem() {
        try {
            return new X11System();
        } catch (UnsatisfiedLinkError error) {
            // here may be possible other linux systems
            throw new UnsupportedSystemException(error.getMessage(), error);
        }
    }

}
