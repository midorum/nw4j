package midorum.nw4j.win32;

import com.sun.jna.platform.win32.Tlhelp32;
import midorum.nw4j.NwThread;

public class Win32Thread implements NwThread {

    private final Tlhelp32.THREADENTRY32 threadentry;

    public Win32Thread(Tlhelp32.THREADENTRY32 threadentry) {
        this.threadentry = threadentry;
    }

    @Override
    public int getThreadId() {
        return threadentry.th32ThreadID;
    }

    @Override
    public int getOwnerProcessId() {
        return threadentry.th32OwnerProcessID;
    }
}
