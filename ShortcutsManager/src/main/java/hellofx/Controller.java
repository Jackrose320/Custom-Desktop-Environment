package hellofx;

import java.io.File;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller {

    @FXML
    private Button closeButton;

    @FXML
    private Button filesButton;

    @FXML
    private HBox buttons;

    @FXML
    public void close() {
        Stage stage = (Stage) this.closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void openFileSystem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setInitialDirectory(new File(".\\resources"));
        fileChooser.showOpenDialog(new Stage());

    }

    public void initialize() {

        this.buttons.setTranslateY(0);

        this.buttons.sceneProperty().addListener((a, b, newScene) -> {
            if (newScene != null) {
                newScene.setOnMouseMoved(event -> {
                    double mouseY = event.getSceneY();
                    double screenHeight = newScene.getHeight();

                    // Check if mouse is at the bottom of the screen
                    if (mouseY >= screenHeight - 50) {
                        // Animate the HBox upwards
                        TranslateTransition transitionUp = new TranslateTransition(
                                Duration.millis(100), this.buttons);
                        transitionUp.setToY(0);
                        transitionUp.play();
                    } else {
                        // Animate the HBox back down
                        TranslateTransition transitionDown = new TranslateTransition(
                                Duration.millis(100), this.buttons);
                        transitionDown.setToY(50);
                        transitionDown.play();
                    }
                });
            }
        });

    }
}
