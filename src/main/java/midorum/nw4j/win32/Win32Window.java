package midorum.nw4j.win32;

import com.sun.jna.Native;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import midorum.nw4j.NwProcess;
import midorum.nw4j.NwResult;
import midorum.nw4j.NwWindow;
import midorum.nw4j.exception.NwAccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static com.sun.jna.platform.win32.WinUser.SWP_NOMOVE;
import static com.sun.jna.platform.win32.WinUser.SWP_NOSIZE;

public class Win32Window implements NwWindow {

    public static final int MAX_STRING_BUFFER_LENGTH = 256;
    private final Logger logger = LoggerFactory.getLogger(Win32Window.class);

    private final WinDef.HWND hWnd;
    private final int threadId;
    private final int processId;
    private Supplier<Win32Process> process = this::obtainAndCacheProcess;

    public Win32Window(WinDef.HWND hWnd) {
        this.hWnd = hWnd;
        final ThreadProcessId threadProcessId = getThreadProcessId();
        this.threadId = threadProcessId.threadId;
        this.processId = threadProcessId.processId;
        logger.trace("Created win32 window: hWnd={}, threadId={}, processId={}", hWnd, threadId, processId);
    }

    @Override
    public int getProcessId() {
        return this.processId;
    }

    @Override
    public NwProcess getProcess() {
        return this.process.get();
    }

    @Override
    public boolean isExists() {
        if (!User32.INSTANCE.IsWindow(hWnd)) return false;
        return this.threadId == getThreadProcessId().threadId;
    }

    @Override
    public boolean isMaximized() {
        logger.debug("check window {} is maximized", hWnd);
        Win32Util.traceStack(logger);
        //var 1
        boolean result = IUser32.INSTANCE.IsZoomed(hWnd).booleanValue();
        Win32Util.traceLastResult(logger, "IUser32.INSTANCE.IsZoomed");
        //var 2
        ///final boolean result = hasStyle(IWinUser.WS_MAXIMIZE);
        logger.debug("window {} is maximized: {}", hWnd, result);
        return result;
    }

    @Override
    public boolean isMinimized() {
        logger.debug("check window {} is minimized", hWnd);
        ///Win32Util.traceStack(logger);
        //var 1
        boolean result = IUser32.INSTANCE.IsIconic(hWnd).booleanValue();
        Win32Util.traceLastResult(logger, "IUser32.INSTANCE.IsIconic");
        //var 2
        ///final boolean result = hasStyle(IWinUser.WS_MINIMIZE);
        logger.debug("window {} is minimized: {}", hWnd, result);
        return result;
    }

    @Override
    public boolean isVisible() {
        logger.debug("check window {} is visible", hWnd);
        final boolean result = IUser32.INSTANCE.IsWindowVisible(hWnd);
        Win32Util.traceLastResult(logger, "IUser32.INSTANCE.IsWindowVisible");
        logger.debug("window {} is visible: {}", hWnd, result);
        return result;
    }

    @Override
    public Rectangle getClientRectangle() {
        logger.debug("get window {} client rect", hWnd);
        final WinDef.RECT result = new WinDef.RECT();
        checkBoolean(IUser32.INSTANCE.GetClientRect(hWnd, result));
        Win32Util.traceLastResult(logger, "User32.INSTANCE.GetClientRect");
        logger.debug("window {} client rect: {}", hWnd, result);
        return result.toRectangle();
    }

    @Override
    public Rectangle getClientToScreenRectangle() {
        logger.debug("get window {} client to screen rect", hWnd);
        final WinDef.RECT result = new WinDef.RECT();
        checkBoolean(IUser32.INSTANCE.ClientToScreen(hWnd, result));
        Win32Util.traceLastResult(logger, "User32.INSTANCE.ClientToScreen");
        logger.debug("window {} client to screen rect: {}", hWnd, result);
        return result.toRectangle();
    }

    @Override
    public Rectangle getWindowRectangle() {
        logger.debug("get window {} rect", hWnd);
        final WinDef.RECT result = getWindowRect();
        logger.debug("window {} rect: {}", hWnd, result);
        return result.toRectangle();
    }

    @Override
    public Optional<String> getTitle() {
        char[] buffer = new char[MAX_STRING_BUFFER_LENGTH];
        User32.INSTANCE.GetWindowText(hWnd, buffer, buffer.length);
        return Win32Util.trimToOptional(Native.toString(buffer));
    }

    @Override
    public Optional<String> getClassName() {
        char[] buffer = new char[MAX_STRING_BUFFER_LENGTH];
        User32.INSTANCE.GetClassName(hWnd, buffer, buffer.length);
        return Win32Util.trimToOptional(Native.toString(buffer));
    }

    @Override
    public NwResult move(int x, int y) {
        logger.debug("try moving window to ({},{})", x, y);
        final WinDef.RECT rect = getWindowRect();
        moveWindow(x, y, rect.right - rect.left, rect.bottom - rect.top);
        return NwResult.success;
    }

    @Override
    public NwResult resize(int width, int height) {
        logger.debug("try resizing window to ({},{})", width, height);
        final WinDef.RECT rect = getWindowRect();
        moveWindow(rect.left, rect.top, rect.left + width, rect.top + height);
        return NwResult.success;
    }

    @Override
    public NwResult moveAndResize(int x, int y, int width, int height) {
        logger.debug("try moving and resizing window to ({},{})({},{})", x, y, width, height);
        moveWindow(x, y, x + width, y + height);
        return NwResult.success;
    }

    @Override
    public NwResult restore() {
        logger.debug("try restoring window {}", hWnd);
        final boolean result = showWindow(IWinUser.SW_RESTORE);
        logger.debug("window {} is restored: {}", hWnd, result);
        return result ? NwResult.success : NwResult.nothing;
    }

    @Override
    public NwResult maximize() {
        logger.debug("try maximizing window {}", hWnd);
        final boolean result = showWindow(IWinUser.SW_MAXIMIZE);
        logger.debug("window {} is maximized: {}", hWnd, result);
        return result ? NwResult.success : NwResult.nothing;
    }

    @Override
    public NwResult minimize() {
        logger.debug("try minimizing window {}", hWnd);
        //var 1
        final boolean result = showWindow(IWinUser.SW_MINIMIZE);
        //var 2
        ///User32.INSTANCE.PostMessage(hwnd, WinUser.WM_SYSCOMMAND, new WinDef.WPARAM(WinUser.SC_MINIMIZE), new WinDef.LPARAM(0));
        logger.debug("window {} is minimized: {}", hWnd, result);
        return result ? NwResult.success : NwResult.nothing;
    }

    @Override
    public NwResult close() {
        logger.debug("try closing window {}", hWnd);
        User32.INSTANCE.PostMessage(hWnd, IWinUser.WM_CLOSE, new WinDef.WPARAM(0), new WinDef.LPARAM(0));
        logger.debug("window {} is closed", hWnd);
        return NwResult.success;
    }

    @Override
    public NwResult bringToFront() {
        if (isMinimized()) {
            restore();
        }
        logger.debug("try bringing window {} to front", hWnd);
        final boolean result = User32.INSTANCE.SetForegroundWindow(hWnd);
        logger.debug("window {} is brought to front: {}", hWnd, result);
        return result ? NwResult.success : NwResult.nothing;
    }

    @Override
    public NwResult setAlwaysOnTop(boolean alwaysOnTop) {
        logger.debug("try setting window {} to always on top: {}", hWnd, alwaysOnTop);
        checkBoolean(User32.INSTANCE.SetWindowPos(hWnd,
                alwaysOnTop ? IWinUser.HWND_TOPMOST : IWinUser.HWND_NOTOPMOST,
                0, 0, 0, 0, SWP_NOMOVE | SWP_NOSIZE));
        logger.debug("window {} is set up to always no top: {}", hWnd, alwaysOnTop);
        return NwResult.success;
    }

    @Override
    public NwResult setTitle(String title) {
        Objects.requireNonNull(title);
        checkBoolean(IUser32.INSTANCE.SetWindowTextW(hWnd, new WString(title))
                .booleanValue());
        return NwResult.success;
    }

    @Override
    public NwResult setKeyboardLayout(int keyboardLayoutId) {
        logger.debug("try setting keyboard layout {}", hWnd);
        bringToFront(); //FIXME нужно перемещение окна на передний план вынести за скобки и производить зависимые от этого операции над специальным типом окна
        User32.INSTANCE.PostMessage(hWnd,
                IWinUser.WM_INPUTLANGCHANGEREQUEST,
                new WinDef.WPARAM(0),
                new WinDef.LPARAM(keyboardLayoutId)
        );
        logger.debug("window {} keyboard layout is set", hWnd);
        return NwResult.success;
    }

    private synchronized Win32Process obtainAndCacheProcess() {
        class Factory implements Supplier<Win32Process> {

            private final Win32Process process = new Win32Process(processId);

            @Override
            public Win32Process get() {
                return this.process;
            }
        }

        if (!(process instanceof Factory)) {
            process = new Factory();
        }

        return process.get();
    }

    private ThreadProcessId getThreadProcessId() {
        final IntByReference processIdRef = new IntByReference();
        int threadId = User32.INSTANCE.GetWindowThreadProcessId(hWnd, processIdRef);
        return new ThreadProcessId(threadId, processIdRef.getValue());
    }

    private record ThreadProcessId(int threadId, int processId) {
    }

    private WinDef.RECT getWindowRect() {
        final WinDef.RECT result = new WinDef.RECT();
        checkBoolean(IUser32.INSTANCE.GetWindowRect(hWnd, result));
        Win32Util.traceLastResult(logger, "User32.INSTANCE.GetWindowRect");
        return result;
    }

    private void moveWindow(int x, int y, int width, int height) {
        checkBoolean(User32.INSTANCE.MoveWindow(hWnd, x, y, width, height, true));
        Win32Util.traceLastResult(logger, "User32.INSTANCE.MoveWindow");
    }

    private boolean showWindow(int flag) {
        final boolean result = User32.INSTANCE.ShowWindow(hWnd, flag);
        Win32Util.traceLastResult(logger, "User32.INSTANCE.ShowWindow");
        return result;
    }

    private WinDef.LRESULT sendCommand(long wParam) {
        return sendCommand(wParam, 0);
    }

    private WinDef.LRESULT sendCommand(long wParam, long lParam) {
        return User32.INSTANCE.SendMessage(hWnd, IWinUser.WM_COMMAND, new WinDef.WPARAM(wParam), new WinDef.LPARAM(lParam));
    }

    private WinDef.HKL getKeyboardLayout() {
        return User32.INSTANCE.GetKeyboardLayout(this.threadId);
    }

    private int getStyle() {
        return checkInt(User32.INSTANCE.GetWindowLong(hWnd, IWinUser.GWL_STYLE));
    }

    private int getExtendedStyle() {
        return checkInt(User32.INSTANCE.GetWindowLong(hWnd, IWinUser.GWL_EXSTYLE));
    }

    private int checkStyle(int mask) {
        int style = getStyle();
        int result = style & mask;
        //System.out.printf("style %08x & %08x = %08x\n", style, mask, result);
        return result;
    }

    private boolean hasStyle(int mask) {
        int result = checkStyle(mask);
        return mask == result;
    }

    private boolean hasAnyStyle(int mask) {
        int result = checkStyle(mask);
        return result != 0;
    }

    private boolean hasAndHasNotStyles(int mustHave, int mustHaveNot) {
        int style = getStyle();
        int has = style & mustHave;
        int hasNot = style & mustHaveNot;
        return has != 0 && hasNot == 0;
    }

    private void checkException() {
        int lastError = Native.getLastError();
        if (lastError == 5) {// ERROR_ACCESS_DENIED
            throw new NwAccessDeniedException("ERROR_ACCESS_DENIED (5)");
        }
    }

    private void checkBoolean(boolean result) {
        checkException();
        if (!result) {
            throw Win32Util.getLastErrorException(hWnd);
        }
    }

    private int checkInt(int result) {
        checkException();
        if (result == 0) {
            throw Win32Util.getLastErrorException(hWnd);
        }
        return result;
    }

}
