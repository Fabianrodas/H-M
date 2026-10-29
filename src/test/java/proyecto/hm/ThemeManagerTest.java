package proyecto.hm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ThemeManagerTest {

    @BeforeAll
    static void startJavaFxToolkit() throws InterruptedException {
        CountDownLatch toolkitReady = new CountDownLatch(1);
        Runnable keepToolkitAlive = () -> {
            Platform.setImplicitExit(false);
            toolkitReady.countDown();
        };
        try {
            Platform.startup(keepToolkitAlive);
        } catch (IllegalStateException alreadyStarted) {
            Platform.runLater(keepToolkitAlive);
        }
        assertTrue(toolkitReady.await(10, TimeUnit.SECONDS), "JavaFX toolkit must start");
    }

    @AfterAll
    static void stopJavaFxToolkit() {
        Platform.exit();
    }

    @Test
    void darkModeSetsReadableTextColorOnCustomMenuLabels() throws InterruptedException {
        AtomicReference<Paint> observedColor = new AtomicReference<>();
        AtomicReference<Paint> observedTextColor = new AtomicReference<>();
        CountDownLatch finished = new CountDownLatch(1);

        Platform.runLater(() -> {
            Stage stage = new Stage();
            Label menuLabel = new Label("Clothing");
            Text menuText = new Text("Dresses");
            HBox content = new HBox(menuLabel, menuText);
            CustomMenuItem customMenuItem = new CustomMenuItem(content);
            Menu menu = new Menu("Kids");
            menu.getItems().add(customMenuItem);
            Scene scene = new Scene(new VBox());
            scene.getStylesheets().add(
                    ThemeManagerTest.class.getResource("/css/estilos.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

            boolean wasDark = ThemeManager.isDarkMode();
            if (!wasDark) {
                ThemeManager.toggle();
            }
            Node routeContent = new AnchorPane(new MenuBar(menu));
            scene.setRoot(new AnchorPane(new ScrollPane(routeContent)));
            ThemeManager.apply(stage.getScene());
            scene.getRoot().applyCss();
            menu.show();
            content.applyCss();
            observedColor.set(menuLabel.getTextFill());
            observedTextColor.set(menuText.getFill());

            if (!wasDark) {
                ThemeManager.toggle();
            }
            menu.hide();
            stage.close();
            finished.countDown();
        });

        assertTrue(finished.await(10, TimeUnit.SECONDS), "Menu color check must finish");
        assertEquals(Color.web("#f0f0f0"), observedColor.get());
        assertEquals(Color.web("#f0f0f0"), observedTextColor.get());
    }

    @Test
    void darkHeroButtonKeepsReadableTextAgainstItsWhiteBackground() throws InterruptedException {
        AtomicReference<Paint> observedColor = new AtomicReference<>();
        AtomicReference<Paint> observedSkinTextColor = new AtomicReference<>();
        CountDownLatch finished = new CountDownLatch(1);

        Platform.runLater(() -> {
            Stage stage = new Stage();
            Button heroButton = new Button("SHOP NOW");
            heroButton.getStyleClass().add("hero-cta");
            heroButton.setStyle(
                    "-fx-background-color: white; -fx-border-color: white; -fx-text-fill: #111111;");
            Scene scene = new Scene(new VBox(heroButton));
            stage.setScene(scene);

            boolean wasDark = ThemeManager.isDarkMode();
            if (!wasDark) {
                ThemeManager.toggle();
            }
            ThemeManager.apply(scene);
            scene.getRoot().applyCss();
            observedColor.set(heroButton.getTextFill());
            observedSkinTextColor.set(((Text) heroButton.lookup(".text")).getFill());

            if (!wasDark) {
                ThemeManager.toggle();
            }
            stage.close();
            finished.countDown();
        });

        assertTrue(finished.await(10, TimeUnit.SECONDS), "Hero button color check must finish");
        assertEquals(Color.web("#111111"), observedColor.get());
        assertEquals(Color.web("#111111"), observedSkinTextColor.get());
    }

    @Test
    void darkPrimaryButtonSkinTextIsReadableAgainstItsWhiteBackground() throws InterruptedException {
        AtomicReference<Paint> observedButtonColor = new AtomicReference<>();
        AtomicReference<Paint> observedSkinTextColor = new AtomicReference<>();
        CountDownLatch finished = new CountDownLatch(1);

        Platform.runLater(() -> {
            Stage stage = new Stage();
            Button signInButton = new Button("Sign in");
            signInButton.setId("signin1");
            Scene scene = new Scene(new VBox(signInButton));
            scene.getStylesheets().add(
                    ThemeManagerTest.class.getResource("/css/estilos.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

            boolean wasDark = ThemeManager.isDarkMode();
            if (!wasDark) {
                ThemeManager.toggle();
            }
            ThemeManager.apply(scene);
            scene.getRoot().applyCss();
            observedButtonColor.set(signInButton.getTextFill());
            observedSkinTextColor.set(((Text) signInButton.lookup(".text")).getFill());

            if (!wasDark) {
                ThemeManager.toggle();
            }
            stage.close();
            finished.countDown();
        });

        assertTrue(finished.await(10, TimeUnit.SECONDS), "Primary button color check must finish");
        assertEquals(Color.web("#111111"), observedButtonColor.get());
        assertEquals(Color.web("#111111"), observedSkinTextColor.get());
    }

    @Test
    void darkSemanticLabelColorsReachTheirVisibleText() throws InterruptedException {
        AtomicReference<Paint> observedAccentColor = new AtomicReference<>();
        AtomicReference<Paint> observedValidationColor = new AtomicReference<>();
        CountDownLatch finished = new CountDownLatch(1);

        Platform.runLater(() -> {
            Stage stage = new Stage();
            Label accentLabel = new Label("H&M");
            accentLabel.getStyleClass().add("brand-accent-text");
            Label validationLabel = new Label("*");
            validationLabel.getStyleClass().add("validation-marker");
            Scene scene = new Scene(new VBox(accentLabel, validationLabel));
            scene.getStylesheets().add(
                    ThemeManagerTest.class.getResource("/css/estilos.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

            boolean wasDark = ThemeManager.isDarkMode();
            if (!wasDark) {
                ThemeManager.toggle();
            }
            ThemeManager.apply(scene);
            scene.getRoot().applyCss();
            observedAccentColor.set(((Text) accentLabel.lookup(".text")).getFill());
            observedValidationColor.set(((Text) validationLabel.lookup(".text")).getFill());

            if (!wasDark) {
                ThemeManager.toggle();
            }
            stage.close();
            finished.countDown();
        });

        assertTrue(finished.await(10, TimeUnit.SECONDS), "Semantic label color check must finish");
        assertEquals(Color.web("#ff6876"), observedAccentColor.get());
        assertEquals(Color.web("#ff7782"), observedValidationColor.get());
    }
}
