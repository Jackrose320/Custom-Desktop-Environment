package hellofx;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public class Shell32 {

    public Shell32() {

    }

    // Interface for the Shell32 library (accessing SHGetFolderPathA)
    public interface Shell32Library extends StdCallLibrary {
        Shell32Library INSTANCE = Native.load("shell32", Shell32Library.class);

        // This function retrieves the known folder path (e.g., Desktop) and handles ANSI paths
        int SHGetFolderPathA(Pointer hwndOwner, int nFolder, Pointer hToken, int dwFlags,
                byte[] pszPath);
    }

    // Constant for the Desktop folder
    public static final int CSIDL_DESKTOP = 0x0000;

    // Method to get the desktop path
    public static String getDesktopPath() {
        byte[] pszPath = new byte[260]; // Standard path length in Windows
        int result = Shell32Library.INSTANCE.SHGetFolderPathA(null, CSIDL_DESKTOP, null,
                0, pszPath);
        if (result == 0) {
            return new String(pszPath, StandardCharsets.UTF_8).trim(); // Convert byte array to String
        } else {
            return null; // Failed to retrieve the path
        }
    }

    // Main method to test the functionality
    public List<String> getFiles() {
        // Step 1: Get the desktop path

        List<String> filePaths = new ArrayList<String>();
        String desktopPath = getDesktopPath();
        if (desktopPath != null) {
            System.out.println("Desktop Path: " + desktopPath);

            // Step 2: Get a list of files on the desktop (you can filter by .lnk files here if desired)
            File desktopDir = new File(desktopPath);
            File[] files = desktopDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Print each file name
                    System.out.println(
                            "Found file: " + desktopPath + "\\" + file.getName());

                    filePaths.add(desktopPath + "\\" + file.getName());
                }
            }
        } else {
            System.out.println("Failed to retrieve desktop path.");
        }

        return filePaths;
    }
}
