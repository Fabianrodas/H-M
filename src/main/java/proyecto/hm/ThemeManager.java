package proyecto.hm;

import java.util.Objects;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Labeled;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

/** Applies one shared light or dark theme to every application scene. */
public final class ThemeManager {

    private static final String DARK_CLASS = "theme-dark";
    private static final String DARK_STYLESHEET = Objects.requireNonNull(
            ThemeManager.class.getResource("/css/tema-oscuro.css"),
            "No se encontró la hoja de estilos del modo oscuro.").toExternalForm();

    private static boolean darkMode;

    private ThemeManager() {
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static boolean toggle() {
        darkMode = !darkMode;
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage stage && stage.getScene() != null) {
                apply(stage.getScene());
            }
        }
        return darkMode;
    }

    public static void apply(Scene scene) {
        Objects.requireNonNull(scene, "La escena es obligatoria.");
        Parent root = scene.getRoot();
        if (darkMode) {
            if (!scene.getStylesheets().contains(DARK_STYLESHEET)) {
                scene.getStylesheets().add(DARK_STYLESHEET);
            }
            if (!root.getStyleClass().contains(DARK_CLASS)) {
                root.getStyleClass().add(DARK_CLASS);
            }
        } else {
            scene.getStylesheets().remove(DARK_STYLESHEET);
            root.getStyleClass().remove(DARK_CLASS);
        }
        root.applyCss();
        synchronizeMenuContent(root, darkMode);
        synchronizeToggleLabels(root, darkMode);
    }

    private static void synchronizeToggleLabels(Parent parent, boolean darkMode) {
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (child instanceof Button button) {
                if (button.getStyleClass().contains("theme-toggle-button")) {
                    button.setText(darkMode ? "Modo claro" : "Modo oscuro");
                    button.setAccessibleText(
                            darkMode ? "Cambiar a modo claro" : "Cambiar a modo oscuro");
                }
            }
            if (child instanceof Parent childParent) {
                synchronizeToggleLabels(childParent, darkMode);
            }
        }
    }

    private static void synchronizeMenuContent(Parent parent, boolean darkMode) {
        if (parent instanceof MenuBar menuBar) {
            menuBar.getMenus().forEach(menu -> synchronizeMenu(menu, darkMode));
        }
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (child instanceof Parent childParent) {
                synchronizeMenuContent(childParent, darkMode);
            }
        }
    }

    private static void synchronizeMenu(MenuItem item, boolean darkMode) {
        if (item instanceof CustomMenuItem customItem
                && customItem.getContent() instanceof Parent content) {
            if (darkMode) {
                if (!content.getStyleClass().contains(DARK_CLASS)) {
                    content.getStyleClass().add(DARK_CLASS);
                }
                if (!content.getStylesheets().contains(DARK_STYLESHEET)) {
                    content.getStylesheets().add(DARK_STYLESHEET);
                }
                content.setStyle("-fx-background-color: #202020;");
            } else {
                content.getStyleClass().remove(DARK_CLASS);
                content.getStylesheets().remove(DARK_STYLESHEET);
                content.setStyle("");
            }
            setMenuTextColor(content, darkMode ? Color.web("#f0f0f0") : Color.BLACK);
        }
        if (item instanceof Menu menu) {
            menu.getItems().forEach(child -> synchronizeMenu(child, darkMode));
        }
    }

    private static void setMenuTextColor(Parent parent, Color color) {
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (child instanceof Labeled labeled) {
                labeled.setTextFill(color);
            } else if (child instanceof Text text) {
                text.setFill(color);
            } else if (child instanceof Parent childParent) {
                setMenuTextColor(childParent, color);
            }
        }
    }
}
