package proyecto.hm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.util.Duration;
import modelo.ClienteSingleton;

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
        if (!isRouteAllowed(fxml)) {
            return;
        }
        scene.setRoot(loadFXML(fxml));
    }
    
    public static void setRootWithTransition(String fxml) throws IOException {
        if (!isRouteAllowed(fxml)) {
            return;
        }
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

    private static boolean isRouteAllowed(String fxml) {
        ClienteSingleton currentUser = ClienteSingleton.getInstance();
        if (currentUser.isPasswordChangeRequired()
                && !"accountsettings".equalsIgnoreCase(fxml)
                && !"bd".equalsIgnoreCase(fxml)) {
            showAccessMessage("Actualiza tu contraseña antes de continuar.");
            return false;
        }
        return true;
    }

    private static void showAccessMessage(String message) {
        HmAlert alert = new HmAlert(HmAlert.Kind.WARNING);
        alert.setTitle("Acceso restringido");
        alert.setHeaderText("No se puede abrir esta pantalla");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
