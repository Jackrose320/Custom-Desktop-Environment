package hellofx.shortcutHelpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hellofx.Shell32;

public class DesktopShortcuts {
    public static List<File> getDesktopShortcuts(String desktopPath) {
        File desktopDir = new File(desktopPath);
        List<File> shortcuts = new ArrayList<>();
        if (desktopDir.exists() && desktopDir.isDirectory()) {
            File[] files = desktopDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    shortcuts.add(file);
                }
            }
        }
        return shortcuts;
    }

    public static void main(String[] args) {
        String desktopPath = Shell32.getDesktopPath(); // Your desktop path
        List<File> shortcuts = getDesktopShortcuts(desktopPath);
        for (File shortcut : shortcuts) {
            System.out.println("Found shortcut: " + shortcut.getName());
        }
    }
}
