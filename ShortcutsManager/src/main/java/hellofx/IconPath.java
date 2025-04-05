package hellofx;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class IconPath {

    public static Image convertSwingIconToJavaFX(Icon swingIcon) {
        if (swingIcon instanceof ImageIcon) {
            ImageIcon imageIcon = (ImageIcon) swingIcon;

            // Get original dimensions
            int width = imageIcon.getIconWidth();
            int height = imageIcon.getIconHeight();

            // Scale up the BufferedImage for better quality
            int scaleFactor = 4; // Adjust as needed for higher resolution
            int scaledWidth = width * scaleFactor;
            int scaledHeight = height * scaleFactor;

            BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImage.createGraphics();

            // Enable high-quality rendering settings
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            // Draw the icon scaled up
            g2d.drawImage(imageIcon.getImage(), 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            // Convert to JavaFX image and return
            return SwingFXUtils.toFXImage(bufferedImage, null);
        }
        return null;
    }

    public static List<Icon> getSwingIcons(List<String> paths) {

        List<Icon> ShortcutIcons = new ArrayList<Icon>();
        FileSystemView fsv = FileSystemView.getFileSystemView();

        for (String shortcut : paths) {

            Path path = Paths.get(shortcut);
            Icon icon = fsv.getSystemIcon(path.toFile());
            System.out.println("Got icon: " + icon);
            ShortcutIcons.add(icon);
        }

        return ShortcutIcons;
    }

    public static Icon getSwingIcon(String fileName) {
        FileSystemView fsv = FileSystemView.getFileSystemView();
        Path path = Paths.get(fileName);
        Icon icon = fsv.getSystemIcon(path.toFile());
        System.out.println("Got icon: " + icon);

        return icon;
    }
}
