package proyecto.hm;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import modelo.DatabaseConfig;

public class LoadingController {

    @FXML
    private ImageView imageView1;

    @FXML
    private ImageView imageView2;

    @FXML
    private Label loadingLabel;
    
    @FXML
    private ProgressBar progressBar;
    
    private final String[] frases = {
        "Preparing the perfect look just for you...",
        "Fetching the latest trends...",
        "Unfolding fashion right to your screen...",
        "Adding the final touches to your look...",
        "Fashion and style are on their way...",
        "Welcome to H&M"
    };

    @FXML
    public void initialize() {
        Image image2 = new Image("/imagenes/logoCarga.png");
        Image image1 = new Image("/imagenes/ueeslogo.png");
        imageView1.setImage(image1);
        imageView2.setImage(image2);
        imageView1.setOpacity(1.0);
        imageView2.setOpacity(0.0);
        progressBar.setStyle("-fx-accent: #6F000F;");
        progressBar.setProgress(0);
        startLoadingThread();
    }

    private void startLoadingThread() {
        Thread loadingThread = new Thread(() -> {
            try {
                Platform.runLater(this::startFadeTransition);
                Platform.runLater(this::startLoadingAnimation);
                Thread.sleep(5000);
                checkDatabaseConnection();
                Platform.runLater(this::transitionToMainScreen);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (SQLException | IllegalStateException e) {
                Platform.runLater(this::showDatabaseError);
            }
        });
        loadingThread.setDaemon(true);
        loadingThread.start();
    }

    private void startFadeTransition() {
        FadeTransition fadeOut1 = new FadeTransition(Duration.seconds(5), imageView1);
        fadeOut1.setFromValue(1.0);
        fadeOut1.setToValue(0.0);
        FadeTransition fadeIn2 = new FadeTransition(Duration.seconds(5), imageView2);
        fadeIn2.setFromValue(0.0);
        fadeIn2.setToValue(1.0);
        fadeOut1.play();
        fadeIn2.play();
    }

    private void startLoadingAnimation() {
        Timeline timeline = new Timeline();
        for (int i = 0; i < frases.length; i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(i * 0.8),
                event -> {
                    loadingLabel.setText(frases[index]);
                    Timeline progressTimeline = new Timeline(
                        new KeyFrame(Duration.ZERO, 
                            new javafx.animation.KeyValue(progressBar.progressProperty(), progressBar.getProgress())),
                        new KeyFrame(Duration.seconds(0.75), 
                            new javafx.animation.KeyValue(progressBar.progressProperty(), (index + 1) / (double) frases.length))
                    );
                    progressTimeline.play();
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.8), loadingLabel);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.play();
                }
            );
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.setCycleCount(1);
        timeline.play();
    }


    private void transitionToMainScreen() {
        try {
            App.setRootWithTransition("tienda");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkDatabaseConnection() throws SQLException {
        try (Connection ignored = DatabaseConfig.loadDefault().openConnection()) {
            // Startup continues only after a successful database connection.
        }
    }

    private void showDatabaseError() {
        ButtonType retry = new ButtonType("Reintentar");
        ButtonType close = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No se pudo iniciar H-M");
        alert.setHeaderText("No fue posible acceder a la base de datos");
        alert.setContentText("Revisa tu archivo .env y confirma que PostgreSQL esté iniciado. Puedes reintentar o cerrar la aplicación.");
        alert.getButtonTypes().setAll(retry, close);
        alert.showAndWait().ifPresent(result -> {
            if (result == retry) {
                startLoadingThread();
            } else {
                Platform.exit();
            }
        });
    }
}
