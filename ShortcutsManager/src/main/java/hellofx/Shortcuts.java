package hellofx;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class Shortcuts {

    final static double buttonWidth = 80; // Width of the button
    final static double buttonHeight = 80; // Height of the button
    final static double verticalSpacing = 10.0; // Space between buttons vertically
    final static double horizontalSpacing = 10.0; // Space between buttons horizontally

    public static void addNewFile(File file, AnchorPane back) {
        Image img = IconPath
                .convertSwingIconToJavaFX(IconPath.getSwingIcon(file.getAbsolutePath()));
        ImageView imageView = new ImageView(img);

        String fileName = file.getName();
        fileName = removeFileExtension(fileName);

        if (fileName.length() > 14) { // "MICROSOFT EDGE" = 14
            fileName = fileName.substring(0, 14) + "...";
        }

        Label label = new Label(fileName);
        label.setStyle("-fx-text-fill: white;");
        label.setOpacity(0.5);

        VBox vbox = new VBox(5, imageView, label);
        vbox.setStyle("-fx-alignment: center;");

        Button button = new Button();
        button.setGraphic(vbox);
        button.setPrefWidth(buttonWidth);
        button.setPrefHeight(buttonHeight);
        button.getStyleClass().setAll("shortcut");
        draggableButton(back, button);

        button.setOnMouseClicked(event -> {
            // Remove "selected" from all buttons
            for (Node node : back.getChildren()) {
                if (node instanceof Button) {
                    node.getStyleClass().remove("selected");
                }
            }
            // Check if it was a double-click
            if (event.getClickCount() == 2) {
                try {
                    // Check if the file exists
                    if (file.exists()) {
                        // Use Desktop to open the file with the default application
                        Desktop.getDesktop().open(file);
                    } else {
                        System.out.println("File not found: " + file.getAbsolutePath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error opening file: " + e.getMessage());
                }
            }

            // Add "selected" class to the clicked button
            button.getStyleClass().add("selected");
            event.consume(); // Prevent further event propagation
        });
        back.getChildren().add(button);
        back.layout();
    }

    public static void draggableButton(AnchorPane back, Button button) {
        // Track the initial position of the mouse click and button position
        final double[] initialMouseX = new double[1];
        final double[] initialMouseY = new double[1];
        final double[] initialButtonX = new double[1];
        final double[] initialButtonY = new double[1];

        // Mouse pressed event to record initial positions
        button.setOnMousePressed(event -> {
            // Record initial mouse position and button position when pressed
            initialMouseX[0] = event.getSceneX();
            initialMouseY[0] = event.getSceneY();
            initialButtonX[0] = AnchorPane.getLeftAnchor(button) == null ? 0
                    : AnchorPane.getLeftAnchor(button);
            initialButtonY[0] = AnchorPane.getTopAnchor(button) == null ? 0
                    : AnchorPane.getTopAnchor(button);

        });

        // Mouse dragged event to update the button's position
        button.setOnMouseDragged(event -> {
            // Calculate how far the mouse has moved since the press
            double deltaX = event.getSceneX() - initialMouseX[0];
            double deltaY = event.getSceneY() - initialMouseY[0];

            // Update the button's position based on the mouse movement
            double newX = initialButtonX[0] + deltaX;
            double newY = initialButtonY[0] + deltaY;

            // Ensure the button doesn't go out of bounds of the AnchorPane (optional)
            newX = Math.max(0, newX); // Prevent from going off the left
            newY = Math.max(0, newY); // Prevent from going off the top

            // Set the new position of the button
            AnchorPane.setLeftAnchor(button, newX);
            AnchorPane.setTopAnchor(button, newY);
        });

        // Optional: You could add a mouse release event to finalize or adjust the position
        button.setOnMouseReleased(event -> {
            // Handle releasing the button (e.g., snapping to grid, or just finalize position)
            // This is a good place to implement logic like snapping the button back into place,
            // saving its position, etc. If you don't need to do anything here, you can leave it empty.
        });
    }

    public static String removeFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName; // Return original name if no extension found
    }

    public static void showShortcuts(Scene scene) {

        AnchorPane back = (AnchorPane) ((AnchorPane) scene.getRoot()).getChildren()
                .get(0);
        double x = 20.0;
        double y = 20.0;

        // Collects all shortcut icons:
        Shell32 s32 = new Shell32();
        List<String> shortcuts = s32.getFiles();
        List<Icon> swingIcons = IconPath.getSwingIcons(shortcuts);
        List<Image> shortcutIMGs = new ArrayList<Image>();
        for (Icon icon : swingIcons) {
            shortcutIMGs.add(IconPath.convertSwingIconToJavaFX(icon));
        }

        // Update the y and x positions based on the button dimensions
        for (int i = 0; i < shortcutIMGs.size(); i++) {
            Image img = shortcutIMGs.get(i);
            String fileName = Paths.get(shortcuts.get(i)).getFileName().toString();
            fileName = removeFileExtension(fileName);

            if (fileName.length() > 14) { // "MICROSOFT EDGE" = 14
                fileName = fileName.substring(0, 14) + "...";
            }

            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(32);
            imageView.setFitHeight(32);

            Label label = new Label(fileName);
            label.setStyle("-fx-text-fill: white;");
            label.setOpacity(0.5);

            VBox vbox = new VBox(5, imageView, label);
            vbox.setStyle("-fx-alignment: center;");

            Button button = new Button();
            button.setGraphic(vbox);
            button.setPrefWidth(buttonWidth);
            button.setPrefHeight(buttonHeight);
            button.getStyleClass().setAll("shortcut");
            draggableButton(back, button);

            final String shortcutPath = shortcuts.get(i);

            button.setOnMouseClicked(event -> {
                // Create a final variable to hold the value of `i` for use inside the lambda

                // Remove "selected" from all buttons
                for (Node node : back.getChildren()) {
                    if (node instanceof Button) {
                        node.getStyleClass().remove("selected");
                    }
                }

                // Check if it was a double-click
                if (event.getClickCount() == 2) {
                    try {
                        // Create a File object using the file path
                        File file = new File(shortcutPath);

                        // Check if the file exists
                        if (file.exists()) {
                            // Use Desktop to open the file with the default application
                            Desktop.getDesktop().open(file);
                        } else {
                            System.out
                                    .println("File not found: " + file.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error opening file: " + e.getMessage());
                    }
                }

                // Add "selected" class to the clicked button
                button.getStyleClass().add("selected");
                event.consume(); // Prevent further event propagation
            });

            AnchorPane.setLeftAnchor(button, x);
            AnchorPane.setTopAnchor(button, y);

            // Update positions for the next button
            y += buttonHeight + verticalSpacing; // Move down for the next button

            // If the next button's position exceeds the scene height, move to the next column
            if (y + buttonHeight > scene.getHeight()) {
                y = 20.0; // Reset to the top
                x += buttonWidth + horizontalSpacing; // Move to the right for the next column
            }

            back.getChildren().add(button);
        }

        scene.setOnMouseClicked(e -> {
            for (Node node : back.getChildren()) {
                if (node instanceof Button) {
                    node.getStyleClass().remove("selected");
                }
            }
        });

    }
}
