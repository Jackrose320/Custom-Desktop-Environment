package hellofx.shortcutHelpers;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.PointerType;

public class WindowManager {

    public interface User32 extends Library {
        User32 INSTANCE = Native.load("user32", User32.class);

        HWND FindWindowA(String lpClassName, String lpWindowName);

        boolean ShowWindow(HWND hWnd, int nCmdShow);
    }

    public static class HWND extends PointerType {
    }

    // Constants for ShowWindow function
    public static final int SW_HIDE = 0;
    public static final int SW_SHOW = 5;

    public static void sendWindowToBackground(String windowTitle) {
        // Find the window handle by title
        HWND hwnd = User32.INSTANCE.FindWindowA(null, windowTitle);

        if (hwnd != null) {
            // Show the window as normal
            User32.INSTANCE.ShowWindow(hwnd, SW_HIDE); // Hide the window
            User32.INSTANCE.ShowWindow(hwnd, SW_SHOW); // Show the window again (in the background)
        }
    }

}
