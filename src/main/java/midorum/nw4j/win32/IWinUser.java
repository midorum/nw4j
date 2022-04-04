package midorum.nw4j.win32;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;

interface IWinUser extends WinUser {

    /*
    MOUSEINPUT structure (winuser.h)
    @see https://docs.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-mouseinput
     */
 /*
    Set if the first X button is pressed or released.
     */
    int XBUTTON1 = 0x0001;
    /*
    Set if the second X button is pressed or released.
     */
    int XBUTTON2 = 0x0002;
    /*
    Movement occurred.
     */
    int MOUSEEVENTF_MOVE = 0x0001;
    /*
    The left button was pressed.
     */
    int MOUSEEVENTF_LEFTDOWN = 0x0002;
    /*
    The left button was released.
     */
    int MOUSEEVENTF_LEFTUP = 0x0004;
    /*
    The right button was pressed.
     */
    int MOUSEEVENTF_RIGHTDOWN = 0x0008;
    /*
    The right button was released.
     */
    int MOUSEEVENTF_RIGHTUP = 0x0010;
    /*
    The middle button was pressed.
     */
    int MOUSEEVENTF_MIDDLEDOWN = 0x0020;
    /*
    The middle button was released.
     */
    int MOUSEEVENTF_MIDDLEUP = 0x0040;
    /*
    An X button was pressed.
     */
    int MOUSEEVENTF_XDOWN = 0x0080;
    /*
    An X button was released.
     */
    int MOUSEEVENTF_XUP = 0x0100;
    /*
    The wheel was moved, if the mouse has a wheel.
    The amount of movement is specified in mouseData.
     */
    int MOUSEEVENTF_WHEEL = 0x0800;
    /*
    The wheel was moved horizontally, if the mouse has a wheel. The amount of movement is specified in mouseData.
    Windows XP/2000: This value is not supported.
     */
    int MOUSEEVENTF_HWHEEL = 0x1000;
    /*
    The WM_MOUSEMOVE messages will not be coalesced. The default behavior is to coalesce WM_MOUSEMOVE messages.
    Windows XP/2000: This value is not supported.
     */
    int MOUSEEVENTF_MOVE_NOCOALESCE = 0x2000;
    /*
    Maps coordinates to the entire desktop. Must be used with MOUSEEVENTF_ABSOLUTE.
     */
    int MOUSEEVENTF_VIRTUALDESK = 0x4000;
    /*
    The dx and dy members contain normalized absolute coordinates.
    If the flag is not set, dxand dy contain relative data (the change in position since the last reported position).
    This flag can be set, or not set, regardless of what kind of mouse or other pointing device, if any, is connected to the system.
    For further information about relative mouse motion, see the following Remarks section.
     */
    int MOUSEEVENTF_ABSOLUTE = 0x8000;

    /*
    KEYBDINPUT structure (winuser.h)
    @see https://docs.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-keybdinput
     */
 /*
    If specified, the scan code was preceded by a prefix byte that has the value 0xE0 (224).
     */
    int KEYEVENTF_EXTENDEDKEY = 0x0001;
    /*
    If specified, the key is being released.
    If not specified, the key is being pressed.
     */
    int KEYEVENTF_KEYUP = 0x0002;
    /*
    If specified, wScan identifies the key and wVk is ignored.
     */
    int KEYEVENTF_SCANCODE = 0x0008;
    /*
    If specified, the system synthesizes a VK_PACKET keystroke.
    The wVk parameter must be zero.
    This flag can only be combined with the KEYEVENTF_KEYUP flag.
     */
    int KEYEVENTF_UNICODE = 0x0004;

//    /*
//     * system commands
//     */
//    /**
//     * Closes the window.
//     */
//    int SC_CLOSE = 0xF060;
//    /**
//     * Changes the cursor to a question mark with a pointer. If the user then clicks a control in the dialog box, the control receives a WM_HELP message.
//     */
//    int SC_CONTEXTHELP = 0xF180;
//    /**
//     * Selects the default item; the user double-clicked the window menu.
//     */
//    int SC_DEFAULT = 0xF160;
//    /**
//     * Activates the window associated with the application-specified hot key. The lParam parameter identifies the window to activate.
//     */
//    int SC_HOTKEY = 0xF150;
//    /**
//     * Scrolls horizontally.
//     */
//    int SC_HSCROLL = 0xF080;
//    /**
//     * Indicates whether the screen saver is secure.
//     */
//    int SCF_ISSECURE = 0x00000001;
//    /**
//     * Retrieves the window menu as a result of a keystroke. For more information, see the Remarks section.
//     */
//    int SC_KEYMENU = 0xF100;
//    /**
//     * Maximizes the window.
//     */
//    int SC_MAXIMIZE = 0xF030;
//    /**
//     * Minimizes the window.
//     */
//    int SC_MINIMIZE = 0xF020;
//    /**
//     * Sets the state of the display. This command supports devices that have power-saving features, such as a battery-powered personal computer.
//     * The lParam parameter can have the following values:
//     * -1 (the display is powering on)
//     * 1 (the display is going to low power)
//     * 2 (the display is being shut off)
//     */
//    int SC_MONITORPOWER = 0xF170;
//    /**
//     * Retrieves the window menu as a result of a mouse click.
//     */
//    int SC_MOUSEMENU = 0xF090;
//    /**
//     * Moves the window.
//     */
//    int SC_MOVE = 0xF010;
//    /**
//     * Moves to the next window.
//     */
//    int SC_NEXTWINDOW = 0xF040;
//    /**
//     * Moves to the previous window.
//     */
//    int SC_PREVWINDOW = 0xF050;
//    /**
//     * Restores the window to its normal position and size.
//     */
//    int SC_RESTORE = 0xF120;
//    /**
//     * Executes the screen saver application specified in the [boot] section of the System.ini file.
//     */
//    int SC_SCREENSAVE = 0xF140;
//    /**
//     * Sizes the window.
//     */
//    int SC_SIZE = 0xF000;
//    /**
//     * Activates the Start menu.
//     */
//    int SC_TASKLIST = 0xF130;
//    /**
//     * Scrolls vertically.
//     */
//    int SC_VSCROLL = 0xF070;

    /*
        MessageBox constants
     */
 /*
        To indicate the buttons displayed in the message box, specify one of the following values.
     */
    /**
     * The message box contains three push buttons: Abort, Retry, and Ignore.
     */
    long MB_ABORTRETRYIGNORE = 0x00000002L;
    /**
     * The message box contains three push buttons: Cancel, Try Again, Continue.
     * Use this message box type instead of MB_ABORTRETRYIGNORE.
     */
    long MB_CANCELTRYCONTINUE = 0x00000006L;
    /**
     * Adds a Help button to the message box. When the user clicks the Help
     * button or presses F1, the system sends a WM_HELP message to the owner.
     */
    long MB_HELP = 0x00004000L;
    /**
     * The message box contains one push button: OK. This is the default.
     */
    long MB_OK = 0x00000000L;
    /**
     * The message box contains two push buttons: OK and Cancel.
     */
    long MB_OKCANCEL = 0x00000001L;
    /**
     * The message box contains two push buttons: Retry and Cancel.
     */
    long MB_RETRYCANCEL = 0x00000005L;
    /**
     * The message box contains two push buttons: Yes and No.
     */
    long MB_YESNO = 0x00000004L;
    /**
     * The message box contains three push buttons: Yes, No, and Cancel.
     */
    long MB_YESNOCANCEL = 0x00000003L;

    /*
        To display an icon in the message box, specify one of the following values.
     */
    /**
     * An exclamation-polong icon appears in the message box.
     */
    long MB_ICONEXCLAMATION = 0x00000030L;
    /**
     * An exclamation-polong icon appears in the message box.
     */
    long MB_ICONWARNING = 0x00000030L;
    /**
     * An icon consisting of a lowercase letter i in a circle appears in the
     * message box.
     */
    long MB_ICONINFORMATION = 0x00000040L;
    /**
     * An icon consisting of a lowercase letter i in a circle appears in the
     * message box.
     */
    long MB_ICONASTERISK = 0x00000040L;
    /**
     * A question-mark icon appears in the message box. The question-mark
     * message icon is no longer recommended because it does not clearly
     * represent a specific type of message and because the phrasing of a
     * message as a question could apply to any message type. In addition, users
     * can confuse the message symbol question mark with Help information.
     * Therefore, do not use this question mark message symbol in your message
     * boxes. The system continues to support its inclusion only for backward
     * compatibility.
     */
    long MB_ICONQUESTION = 0x00000020L;
    /**
     * A stop-sign icon appears in the message box.
     */
    long MB_ICONSTOP = 0x00000010L;
    /**
     * A stop-sign icon appears in the message box.
     */
    long MB_ICONERROR = 0x00000010L;
    /**
     * A stop-sign icon appears in the message box.
     */
    long MB_ICONHAND = 0x00000010L;

    /*
        To indicate the default button, specify one of the following values.
     */
    /**
     * The first button is the default button. MB_DEFBUTTON1 is the default
     * unless MB_DEFBUTTON2, MB_DEFBUTTON3, or MB_DEFBUTTON4 is specified.
     */
    long MB_DEFBUTTON1 = 0x00000000L;

    /**
     * The second button is the default button.
     */
    long MB_DEFBUTTON2 = 0x00000100L;
    /**
     * The third button is the default button.
     */
    long MB_DEFBUTTON3 = 0x00000200L;
    /**
     * The fourth button is the default button.
     */
    long MB_DEFBUTTON4 = 0x00000300L;

    /*
        To indicate the modality of the dialog box, specify one of the following values.
     */
    /**
     * The user must respond to the message box before continuing work in the
     * window identified by the hWnd parameter. However, the user can move to
     * the windows of other threads and work in those windows. Depending on the
     * hierarchy of windows in the application, the user may be able to move to
     * other windows within the thread. All child windows of the parent of the
     * message box are automatically disabled, but pop-up windows are not.
     * <p>
     * MB_APPLMODAL is the default if neither MB_SYSTEMMODAL nor MB_TASKMODAL is
     * specified.
     */
    long MB_APPLMODAL = 0x00000000L;

    /**
     * Same as MB_APPLMODAL except that the message box has the WS_EX_TOPMOST
     * style. Use system-modal message boxes to notify the user of serious,
     * potentially damaging errors that require immediate attention (for
     * example, running out of memory). This flag has no effect on the user's
     * ability to interact with windows other than those associated with hWnd.
     */
    long MB_SYSTEMMODAL = 0x00001000L;
    /**
     * Same as MB_APPLMODAL except that all the top-level windows belonging to
     * the current thread are disabled if the hWnd parameter is NULL. Use this
     * flag when the calling application or library does not have a window
     * handle available but still needs to prevent input to other windows in the
     * calling thread without suspending other threads.
     */
    long MB_TASKMODAL = 0x00002000L;

    /*
        To specify other options, use one or more of the following values.
     */
    /**
     * Same as desktop of the interactive window station. For more information,
     * see Window Stations. If the current input desktop is not the default
     * desktop, MessageBox does not return until the user switches to the
     * default desktop.
     */
    long MB_DEFAULT_DESKTOP_ONLY = 0x00020000L;

    /**
     * The text is right-justified.
     */
    long MB_RIGHT = 0x00080000L;
    /**
     * Displays message and caption text using right-to-left reading order on
     * Hebrew and Arabic systems.
     */
    long MB_RTLREADING = 0x00100000L;
    /**
     * The message box becomes the foreground window. Internally, the system
     * calls the SetForegroundWindow function for the message box.
     */
    long MB_SETFOREGROUND = 0x00010000L;
    /**
     * The message box is created with the WS_EX_TOPMOST window style.
     */
    long MB_TOPMOST = 0x00040000L;
    /**
     * The caller is a service notifying the user of an event. The function
     * displays a message box on the current active desktop, even if there is no
     * user logged on to the computer. Terminal Services: If the calling thread
     * has an impersonation token, the function directs the message box to the
     * session specified in the impersonation token.
     * <p>
     * If this flag is set, the hWnd parameter must be NULL. This is so that the
     * message box can appear on a desktop other than the desktop corresponding
     * to the hWnd.
     * <p>
     * For information on security considerations in regard to using this flag,
     * see Interactive Services. In particular, be aware that this flag can
     * produce interactive content on a locked desktop and should therefore be
     * used for only a very limited set of scenarios, such as resource
     * exhaustion.
     */
    long MB_SERVICE_NOTIFICATION = 0x00200000L;

    /*
        MessageBox return values
     */
    /**
     * The Abort button was selected.
     */
    int IDABORT = 3;
    /**
     * The Cancel button was selected.
     */
    int IDCANCEL = 2;
    /**
     * The Continue button was selected.
     */
    int IDCONTINUE = 11;
    /**
     * The Ignore button was selected.
     */
    int IDIGNORE = 5;
    /**
     * The No button was selected.
     */
    int IDNO = 7;
    /**
     * The OK button was selected.
     */
    int IDOK = 1;
    /**
     * The Retry button was selected.
     */
    int IDRETRY = 4;
    /**
     * The Try Again button was selected.
     */
    int IDTRYAGAIN = 10;
    /**
     * The Yes button was selected.
     */
    int IDYES = 6;

    /*
    SetWindowPos
    https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-setwindowpos
     */
    /**
     * Places the window at the bottom of the Z order. If the hWnd parameter
     * identifies a topmost window, the window loses its topmost status and is
     * placed at the bottom of all other windows.
     */
    HWND HWND_BOTTOM = new HWND(Pointer.createConstant(1));
    /**
     * Places the window above all non-topmost windows (that is, behind all
     * topmost windows). This flag has no effect if the window is already a
     * non-topmost window.
     */
    HWND HWND_NOTOPMOST = new HWND(Pointer.createConstant(-2));
    /**
     * Places the window at the top of the Z order.
     */
    HWND HWND_TOP = new HWND(Pointer.createConstant(0));
    /**
     * Places the window above all non-topmost windows. The window maintains its
     * topmost position even when it is deactivated.
     */
    HWND HWND_TOPMOST = new HWND(Pointer.createConstant(-1));

    /**
     * Posted to the window with the focus when the user chooses a new input language,
     * either with the hotkey (specified in the Keyboard control panel application)
     * or from the indicator on the system taskbar.
     * @see <a href="https://docs.microsoft.com/en-us/windows/win32/winmsg/wm-inputlangchangerequest">...</a>
     */
    int WM_INPUTLANGCHANGEREQUEST = 0x0050;

    /**
     * Application-defined callback function used with the EnumPropsEx function.
     * The function receives property entries from a window's property list.
     *
     * @see <a href="https://docs.microsoft.com/en-us/windows/win32/api/winuser/nc-winuser-propenumprocexw">...</a>
     * PROPENUMPROCEXW Propenumprocexw; BOOL Propenumprocexw( HWND
     * unnamedParam1, LPWSTR unnamedParam2, HANDLE unnamedParam3, ULONG_PTR
     * unnamedParam4 ) {...}
     */
    public interface PROPENUMPROCEXW extends StdCallLibrary.StdCallCallback {

        /**
         * Return whether to continue enumeration.
         *
         * @param hWnd     window handle
         * @param lpwstr
         * @param handle
         * @param ulongPtr
         * @return FIXME
         */
        boolean callback(HWND hWnd, WTypes.LPWSTR lpwstr, WinNT.HANDLE handle, BaseTSD.ULONG_PTR ulongPtr);
    }

    /*
    Window Messages
     */
    int WM_COMMAND = 0x0111;

    /**
     * Posted to a window when the cursor moves.
     * If the mouse is not captured, the message is posted to the window that contains the cursor.
     * Otherwise, the message is posted to the window that has captured the mouse.
     *
     * https://docs.microsoft.com/en-us/windows/win32/inputdev/wm-mousemove?redirectedfrom=MSDN
     */
    int WM_MOUSEMOVE = 0x0200;

    /**
     * Posted when the user presses the left mouse button while the cursor is in the client area of a window.
     * If the mouse is not captured, the message is posted to the window beneath the cursor.
     * Otherwise, the message is posted to the window that has captured the mouse.
     *
     * https://docs.microsoft.com/en-us/windows/win32/inputdev/wm-lbuttondown?redirectedfrom=MSDN
     */
    int WM_LBUTTONDOWN = 0x0201;

    /**
     * Posted when the user releases the left mouse button while the cursor is in the client area of a window.
     * If the mouse is not captured, the message is posted to the window beneath the cursor.
     * Otherwise, the message is posted to the window that has captured the mouse.
     *
     * https://docs.microsoft.com/en-us/windows/win32/inputdev/wm-lbuttonup?redirectedfrom=MSDN
     */
    int WM_LBUTTONUP = 0x0202;

    /**
     * Posted when the user presses the right mouse button while the cursor is in the client area of a window.
     * If the mouse is not captured, the message is posted to the window beneath the cursor.
     * Otherwise, the message is posted to the window that has captured the mouse.
     *
     * https://docs.microsoft.com/en-us/windows/win32/inputdev/wm-rbuttondown?redirectedfrom=MSDN
     */
    int WM_RBUTTONDOWN = 0x0204;

    /**
     * Posted when the user releases the right mouse button while the cursor is in the client area of a window.
     * If the mouse is not captured, the message is posted to the window beneath the cursor.
     * Otherwise, the message is posted to the window that has captured the mouse.
     *
     * https://docs.microsoft.com/en-us/windows/win32/inputdev/wm-rbuttonup?redirectedfrom=MSDN
     */
    int WM_RBUTTONUP = 0x0205;

    /**
     * Sent to the focus window when the mouse wheel is rotated.
     * The DefWindowProc function propagates the message to the window's parent. There should be no internal forwarding of the message,
     * since DefWindowProc propagates it up the parent chain until it finds a window that processes it.
     *
     * https://docs.microsoft.com/en-us/windows/win32/inputdev/wm-mousewheel?redirectedfrom=MSDN
     */
    int WM_MOUSEWHEEL = 0x020A;

    /**
     * Sent to the active window when the mouse's horizontal scroll wheel is tilted or rotated.
     * The DefWindowProc function propagates the message to the window's parent. There should be no internal forwarding of the message,
     * since DefWindowProc propagates it up the parent chain until it finds a window that processes it.
     *
     * https://docs.microsoft.com/en-us/windows/win32/inputdev/wm-mousehwheel?redirectedfrom=MSDN
     */
    int WM_MOUSEHWHEEL = 0x020E;

    /*
    Window Commmands
     */
    long MIN_ALL_UNDO = 416L;
    long MIN_ALL = 419L;
}
