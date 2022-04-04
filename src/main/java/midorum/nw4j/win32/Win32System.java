package midorum.nw4j.win32;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import midorum.nw4j.NwProcess;
import midorum.nw4j.NwSystem;
import midorum.nw4j.NwThread;
import midorum.nw4j.NwWindow;
import midorum.nw4j.exception.NwLastErrorException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class Win32System implements NwSystem {

    @Override
    public List<NwWindow> listWindows() {
        final List<NwWindow> windows = new ArrayList<>(10);
        final boolean result = User32.INSTANCE.EnumWindows((WinDef.HWND hWnd, Pointer pointer) -> {
            windows.add(new Win32Window(hWnd));
            return true;
        }, Pointer.NULL);
        //logger.info("EnumWindows: " + Kernel32Util.getLastErrorMessage());
        if (!result) {
            throw Win32Util.getLastErrorException();
        }
        return windows;
    }

    @Override
    public List<NwProcess> listProcesses() {
        return useToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, hSnapshot -> {
            List<NwProcess> processes = new ArrayList<>();

            Tlhelp32.PROCESSENTRY32 processentry32 = new Tlhelp32.PROCESSENTRY32();
            if (!Kernel32.INSTANCE.Process32First(hSnapshot, processentry32)) {
                throw Win32Util.getLastErrorException();
            }

            do {
                processes.add(new Win32Process(processentry32));
                processentry32 = new Tlhelp32.PROCESSENTRY32();
            } while (Kernel32.INSTANCE.Process32Next(hSnapshot, processentry32));

            int lastError = Kernel32.INSTANCE.GetLastError();
            if (lastError != W32Errors.ERROR_SUCCESS && lastError != W32Errors.ERROR_NO_MORE_FILES) {
                throw new NwLastErrorException(Win32Util.getErrorMessage(lastError));
            }

            return processes;
        });
    }

    @Override
    public List<NwThread> listThreads() {
        return useToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPTHREAD, hSnapshot -> {

            Tlhelp32.THREADENTRY32 first = new Tlhelp32.THREADENTRY32();
            if (!Kernel32.INSTANCE.Thread32First(hSnapshot, first)) {
                throw Win32Util.getLastErrorException();
            }

            List<NwThread> threads = new ArrayList<>();
            threads.add(new Win32Thread(first));

            Tlhelp32.THREADENTRY32 next = new Tlhelp32.THREADENTRY32();
            while (Kernel32.INSTANCE.Thread32Next(hSnapshot, next)) {
                threads.add(new Win32Thread(next));
                next = new Tlhelp32.THREADENTRY32();
            }

            int lastError = Kernel32.INSTANCE.GetLastError();
            if (lastError != W32Errors.ERROR_SUCCESS && lastError != W32Errors.ERROR_NO_MORE_FILES) {
                throw new NwLastErrorException(Win32Util.getErrorMessage(lastError));
            }

            return threads;
        });
    }

    @Override
    public int getNumberOfProcessors() {
        final WinBase.SYSTEM_INFO systemInfo = getSystemInfo();
        return systemInfo.dwNumberOfProcessors.intValue();
    }

    @Override
    public long getSystemTime() {
        final WinBase.SYSTEMTIME systemTime = new WinBase.SYSTEMTIME();
        Kernel32.INSTANCE.GetSystemTime(systemTime);
        return systemTime.toCalendar().getTimeInMillis();
    }

    @Override
    public Rectangle getScreenResolution() {
        return new Rectangle(User32.INSTANCE.GetSystemMetrics(WinUser.SM_CXSCREEN),
                User32.INSTANCE.GetSystemMetrics(WinUser.SM_CYSCREEN));
    }

    @Override
    public Optional<NwWindow> getFocusedWindow() {
        return Optional.ofNullable(User32.INSTANCE.GetForegroundWindow()).map(Win32Window::new);
    }

    @Override
    public void close() throws Exception {
        //nothing to close
    }

    private WinBase.SYSTEM_INFO getSystemInfo() {
        final WinBase.SYSTEM_INFO systemInfo = new WinBase.SYSTEM_INFO();
        Kernel32.INSTANCE.GetSystemInfo(systemInfo);
        return systemInfo;
    }

    private <T> T useToolhelp32Snapshot(WinDef.DWORD dwFlags, Function<WinNT.HANDLE, T> function) {
        return useToolhelp32SnapshotForProcessId(new WinDef.DWORD(0), dwFlags, function);
    }

    private <T> T useToolhelp32SnapshotForProcessId(WinDef.DWORD processId, WinDef.DWORD flags, Function<WinNT.HANDLE, T> function) {
        Objects.requireNonNull(processId, "processId can't be null");
        Objects.requireNonNull(flags, "flags can't be null");
        Objects.requireNonNull(function, "function can't be null");
        // Take a snapshot of the system.
        WinNT.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(flags, processId);
        if (snapshot == null) {
            throw new NwLastErrorException("Error while getting system snapshot: " + Win32Util.getLastErrorMessage());
        }
        NwLastErrorException we = null;
        try {
            return function.apply(snapshot);
        } catch (Win32Exception e) {
            we = new NwLastErrorException(e.getErrorCode(), e.getMessage());
            throw we;
        } finally {
            try {
                Kernel32Util.closeHandle(snapshot);
            } catch (Win32Exception e) {
                if (we == null) {
                    we = new NwLastErrorException(e.getErrorCode(), e.getMessage());
                } else {
                    we.addSuppressed(e);
                }
            }
            if (we != null) {
                throw we;
            }
        }
    }
}
