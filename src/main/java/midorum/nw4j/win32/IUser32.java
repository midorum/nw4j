package midorum.nw4j.win32;

import com.sun.jna.Native;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;

public interface IUser32 extends User32 {
    //public interface IUser32 extends Library {
//    IUser32 INSTANCE = Native.load("user32", IUser32.class, W32APIOptions.UNICODE_OPTIONS);
    IUser32 INSTANCE = Native.load("user32", IUser32.class, W32APIOptions.DEFAULT_OPTIONS);
    int MB_YESNO = 0x00000004;
    int MB_ICONEXCLAMATION = 0x00000030;

    /*
    https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-messagebox
    int MessageBox(
      HWND    hWnd,
      LPCTSTR lpText,
      LPCTSTR lpCaption,
      UINT    uType
    );
     */
    int MessageBox(WinDef.HWND hWnd, String lpText, String lpCaption, WinDef.UINT flags);

    int MessageBoxW(WinDef.HWND hWnd, WString text, WString caption, WinDef.UINT flags);

    /*
    https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-isiconic
    BOOL IsIconic(
      HWND hWnd
    );
     */
    WinDef.BOOL IsIconic(WinDef.HWND hWnd);

    /*
    https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-iszoomed
    BOOL IsZoomed(
      [in] HWND hWnd
    );
    */
    WinDef.BOOL IsZoomed(WinDef.HWND hWnd);

    /*
    https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-setpropw
    BOOL SetPropW(
      HWND    hWnd,
      LPCWSTR lpString,
      HANDLE  hData
    );
     */
    WinDef.BOOL SetPropW(WinDef.HWND hWnd, WString lpString, WinNT.HANDLE hData);

    /*
    https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getpropw
    HANDLE GetPropW(
      HWND    hWnd,
      LPCWSTR lpString
    );
     */
    WinNT.HANDLE GetPropW(WinDef.HWND hWnd, WString lpString);

    /*
    https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-removepropw
    HANDLE RemovePropW(
      HWND    hWnd,
      LPCWSTR lpString
    );
     */
    WinNT.HANDLE RemovePropW(WinDef.HWND hWnd, WString lpString);

    /*
    https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-enumpropsexw
    int EnumPropsExW(
      HWND            hWnd,
      PROPENUMPROCEXW lpEnumFunc,
      LPARAM          lParam
    );
     */
    int EnumPropsExW(WinDef.HWND hWnd, IWinUser.PROPENUMPROCEXW lpEnumFunc, WinDef.LPARAM lParam);

    /*
    https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-setwindowtextw
    BOOL SetWindowTextW(
      HWND    hWnd,
      LPCWSTR lpString
    );
     */
    WinDef.BOOL SetWindowTextW(WinDef.HWND hWnd, WString lpString);

    /**
     * https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-clienttoscreen?redirectedfrom=MSDN
     */
    boolean ClientToScreen(WinDef.HWND hWnd, WinDef.RECT rect);

    /**
     * Retrieves the status of the specified virtual key. The status specifies whether the key is up, down, or toggled
     * (on, off—alternating each time the key is pressed).
     *
     * @see https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getkeystate
     * @param nVirtKey A virtual key. If the desired virtual key is a letter or digit (A through Z, a through z, or 0 through 9),
     *                nVirtKey must be set to the ASCII value of that character. For other keys, it must be a virtual-key code.
     *
     *                 If a non-English keyboard layout is used, virtual keys with values in the range ASCII A through Z
     *                 and 0 through 9 are used to specify most of the character keys.
     *                 For example, for the German keyboard layout, the virtual key of value ASCII O (0x4F) refers to the "o" key,
     *                 whereas VK_OEM_1 refers to the "o with umlaut" key.
     * @return The return value specifies the status of the specified virtual key, as follows:
     *
     * If the high-order bit is 1, the key is down; otherwise, it is up.
     * If the low-order bit is 1, the key is toggled. A key, such as the CAPS LOCK key, is toggled if it is turned on.
     * The key is off and untoggled if the low-order bit is 0. A toggle key's indicator light (if any) on the keyboard
     * will be on when the key is toggled, and off when the key is untoggled.
     */
    WinDef.SHORT GetKeyState(int nVirtKey);

    /**
     * Returns the system DPI.
     *
     * The return value will be dependent based upon the calling context.
     * If the current thread has a DPI_AWARENESS value of DPI_AWARENESS_UNAWARE, the return value will be 96. That is because
     * the current context always assumes a DPI of 96.
     * For any other DPI_AWARENESS value, the return value will be the actual system DPI.
     *
     * You should not cache the system DPI, but should use GetDpiForSystem whenever you need the system DPI value.
     *
     * @return The system DPI value.
     *
     * @see <a href="https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-getdpiforsystem">GetDpiForSystem function</a>
     */
    WinDef.UINT GetDpiForSystem();
}
