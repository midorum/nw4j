package midorum.nw4j.win32;

import com.sun.jna.platform.win32.Tlhelp32;
import midorum.nw4j.NwProcess;

import java.util.Optional;

public class Win32Process implements NwProcess {

    private final int pid;
    private final String name;
    private final Tlhelp32.PROCESSENTRY32 processEntry;

    private Win32Process(int pid, String name, Tlhelp32.PROCESSENTRY32 processEntry) {
        this.pid = pid;
        this.name = name;
        this.processEntry = processEntry;
    }

    public Win32Process(int pid) {
        this(pid, null, null);
    }

    public Win32Process(Tlhelp32.PROCESSENTRY32 processEntry) {
        this(processEntry.th32ProcessID.intValue(), String.valueOf(processEntry.szExeFile).trim(), processEntry);
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(processEntry)
                .map(processentry32 -> String.valueOf(processEntry.szExeFile).trim());
    }
}
