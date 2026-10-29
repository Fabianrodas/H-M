package proyecto.hm;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/** A fully custom, FXML-backed modal alert styled for the H-M application. */
public final class HmAlert {

    public enum Kind {
        INFORMATION("i", "Información"),
        SUCCESS("✓", "Éxito"),
        WARNING("!", "Advertencia"),
        ERROR("×", "Error"),
        CONFIRMATION("?", "Confirmación");

        private final String symbol;
        private final String accessibleName;

        Kind(String symbol, String accessibleName) {
            this.symbol = symbol;
            this.accessibleName = accessibleName;
        }

        String getSymbol() {
            return symbol;
        }

        String getAccessibleName() {
            return accessibleName;
        }
    }

    private final Kind kind;
    private final ObservableList<ButtonType> buttonTypes = FXCollections.observableArrayList();
    private String title;
    private String headerText;
    private String contentText;

    public HmAlert(Kind kind) {
        this.kind = Objects.requireNonNull(kind, "El tipo de alerta es obligatorio.");
        if (kind == Kind.CONFIRMATION) {
            buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL);
        } else {
            buttonTypes.add(ButtonType.OK);
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public ObservableList<ButtonType> getButtonTypes() {
        return buttonTypes;
    }

    public Optional<ButtonType> showAndWait() {
        if (buttonTypes.isEmpty()) {
            buttonTypes.add(ButtonType.OK);
        }

        try {
            FXMLLoader loader = new FXMLLoader(HmAlert.class.getResource("/proyecto/hm/alerta.fxml"));
            Parent root = loader.load();
            HmAlertController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);

            Window owner = findActiveOwner();
            if (owner != null) {
                stage.initOwner(owner);
                stage.initModality(Modality.WINDOW_MODAL);
            } else {
                stage.initModality(Modality.APPLICATION_MODAL);
            }

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            ThemeManager.apply(scene);
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    controller.dismiss();
                    event.consume();
                }
            });
            stage.setScene(scene);
            stage.setTitle(title == null ? "H&M" : title);
            addApplicationIcon(stage);
            controller.configure(stage, kind, title, headerText, contentText, List.copyOf(buttonTypes));
            stage.sizeToScene();
            centerOverOwner(stage, owner);
            stage.setOnShown(event -> centerOverOwner(stage, owner));

            stage.showAndWait();
            return Optional.ofNullable(controller.getResult());
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo cargar la alerta personalizada.", exception);
        }
    }

    private static void addApplicationIcon(Stage stage) {
        var iconUrl = HmAlert.class.getResource("/imagenes/h&m.png");
        if (iconUrl != null) {
            stage.getIcons().add(new Image(iconUrl.toExternalForm()));
        }
    }

    private static void centerOverOwner(Stage alertStage, Window owner) {
        if (owner == null) {
            alertStage.centerOnScreen();
            return;
        }

        alertStage.setX(owner.getX() + (owner.getWidth() - alertStage.getWidth()) / 2);
        alertStage.setY(owner.getY() + (owner.getHeight() - alertStage.getHeight()) / 2);
    }

    private static Window findActiveOwner() {
        Window fallback = null;
        for (Window window : Window.getWindows()) {
            if (!(window instanceof Stage) || !window.isShowing()) {
                continue;
            }
            fallback = window;
            if (window.isFocused()) {
                return window;
            }
        }
        return fallback;
    }

    static String styleClass(Kind kind) {
        return "hm-alert-" + kind.name().toLowerCase(Locale.ROOT);
    }
}
