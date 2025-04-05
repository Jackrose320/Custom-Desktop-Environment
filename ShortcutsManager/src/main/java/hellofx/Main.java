package hellofx;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import hellofx.shortcutHelpers.WindowManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    private MediaPlayer mediaPlayer;

    public void playMusicOnRepeat(File file) {
        // Create a Media object using the file path
        if (!file.exists()) {
            System.out.println("File not found: " + file.getName());
            return;
        }

        // Create the Media object
        Media media = new Media(file.toURI().toString());

        // Create the MediaPlayer object
        this.mediaPlayer = new MediaPlayer(media);

        // Set the cycle count to indefinite so it plays on repeat
        this.mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // Play the music
        this.mediaPlayer.play();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(this.getClass().getResource("/fxml/hello.fxml"));
        primaryStage.setTitle("");
        primaryStage.setAlwaysOnTop(false);
        // primaryStage.setFullScreen(true);
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight() - 0.5);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.iconifiedProperty().addListener((obs, oldVal, newVal) -> {
            if (!primaryStage.isIconified()) {
                primaryStage.toBack(); // Sends to back after restoring
            }
        });

        File imageFile = new File("resources\\bg.jpg");
        if (!imageFile.exists()) {
            System.out.println(
                    "Background image not found: " + imageFile.getAbsolutePath());
            return;
        }
        Image bgImage = new Image(imageFile.toURI().toString());

        BackgroundImage backgroundImage = new BackgroundImage(bgImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, new BackgroundSize(BackgroundSize.AUTO,
                        BackgroundSize.AUTO, true, true, true, true));

        if (root instanceof AnchorPane) {
            ((AnchorPane) root).setBackground(new Background(backgroundImage));

            ((AnchorPane) root).getChildren().get(0).setOnDragOver(event -> {
                // Accept only file drag operations
                if (event.getGestureSource() != ((AnchorPane) root).getChildren().get(0)
                        && event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            });

            ((AnchorPane) root).getChildren().get(0).setOnDragDropped(event -> {
                // Get the file that was dropped
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasFiles()) {
                    // Get the file from the drag event
                    File file = dragboard.getFiles().get(0);

                    // Create a label or icon for the dropped file
                    Label fileIcon = new Label(file.getName());
                    fileIcon.setStyle(
                            "-fx-background-color: lightgray; -fx-padding: 5px; -fx-border-color: black;");
                    fileIcon.setLayoutX(event.getX());
                    fileIcon.setLayoutY(event.getY());
                    fileIcon.setOnMousePressed(e -> {
                        fileIcon.setUserData(new Point2D(e.getX(), e.getY()));
                    });

                    fileIcon.setOnMouseDragged(e -> {
                        Point2D offset = (Point2D) fileIcon.getUserData();
                        fileIcon.setLayoutX(e.getSceneX() - offset.getX());
                        fileIcon.setLayoutY(e.getSceneY() - offset.getY());
                    });

                    // Optional: store file reference if you want to open it later
                    fileIcon.setOnMouseClicked(e -> {
                        try {
                            if (e.getClickCount() == 2) {
                                Desktop.getDesktop().open(file);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });

                    // Add the visual element to the AnchorPane
                    ((AnchorPane) root).getChildren().add(fileIcon);

                    // Get the user directory (current working directory)
                    String userDir = Shell32.getDesktopPath();

                    // Create the destination path
                    Path destinationPath = new File(userDir, file.getName()).toPath();

                    try {
                        // Copy the file to the destination
                        Files.copy(file.toPath(), destinationPath,
                                StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("File copied to: " + destinationPath);

                        // Add the visual element to the AnchorPane
                        ((AnchorPane) root).getChildren().add(fileIcon);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error copying file.");
                    }
                }
                event.setDropCompleted(true);
                event.consume();
            });

        }

        File musicFile = new File("resources\\music.mp3");
        if (musicFile.exists()) {
            this.playMusicOnRepeat(musicFile);
        }
        Scene scene = new Scene(root, 400, 300, Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.show();
        Shortcuts.showShortcuts(scene);
        primaryStage.toBack();
        WindowManager.sendWindowToBackground(primaryStage.getTitle());

        primaryStage.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                // Ensure the music is playing when the window is focused again
                if (this.mediaPlayer != null && !this.mediaPlayer.getStatus()
                        .equals(MediaPlayer.Status.PLAYING)) {
                    this.mediaPlayer.play();
                }
            }
        });

    }

    public static void main(String[] args) {

        launch(args);
    }
}
