package proyecto.tiendaproyecto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("pantalla_carga"), 750, 437);
        Image icon = new Image(getClass().getResourceAsStream("/imagenes/h&m.png"));
        String css = getClass().getResource("/css/estilos.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.getIcons().add(icon);
        stage.setResizable(false);
        double x = 400;
        double y = 150;
        stage.setX(x);
        stage.setY(y);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    public static void setRootWithTransition(String fxml) throws IOException {
        Parent newRoot = loadFXML(fxml);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), scene.getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            scene.setRoot(newRoot);
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), scene.getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}