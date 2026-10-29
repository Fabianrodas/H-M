package proyecto.hm;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.util.HashSet;
import java.util.Set;

/** Applies width-based layout constraints without scaling the rendered scene. */
final class ResponsiveScreenLayout {

    private static final double DESIGN_WIDTH = 750;
    private static final String BASE_TRANSLATE_X = "responsive-base-translate-x";
    private static final String BASE_FOOTER_RIGHT = "responsive-base-footer-right";

    private ResponsiveScreenLayout() {
    }

    static Parent prepare(Parent root) {
        if (!(root instanceof AnchorPane screen)) {
            return root;
        }

        screen.setMinWidth(0);
        screen.setMaxWidth(Double.MAX_VALUE);
        ScrollPane scrollPane = screen.getChildrenUnmodifiable().stream()
                .filter(ScrollPane.class::isInstance)
                .map(ScrollPane.class::cast)
                .findFirst()
                .orElse(null);

        if (scrollPane == null) {
            centerFixedSplashContent(screen);
            return screen;
        }

        scrollPane.setMinSize(0, 0);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);

        if (!(scrollPane.getContent() instanceof AnchorPane content)) {
            return screen;
        }

        content.setMinWidth(0);
        content.setMaxWidth(Double.MAX_VALUE);
        Set<Node> responsivePanels = new HashSet<>(ResponsiveProductGrid.installCatalogGrids(content));
        configureSearchLayout(content);
        installWidthLayout(content, responsivePanels);
        return screen;
    }

    private static void configureSearchLayout(AnchorPane content) {
        Node searchField = directChildById(content, "txtbuscar");
        if (searchField != null) {
            AnchorPane.setLeftAnchor(searchField, 30.0);
            AnchorPane.setRightAnchor(searchField, 33.0);
            if (searchField instanceof Region region) {
                region.setMinWidth(0);
                region.setMaxWidth(Double.MAX_VALUE);
            }
        }

        Node results = directChildById(content, "vboxResultados");
        if (results != null) {
            AnchorPane.setLeftAnchor(results, 30.0);
            AnchorPane.setRightAnchor(results, 32.0);
            if (results instanceof Region region) {
                region.setMinWidth(0);
                region.setMaxWidth(Double.MAX_VALUE);
            }
        }
    }

    private static void installWidthLayout(AnchorPane content, Set<Node> responsivePanels) {
        for (Node node : content.getChildren()) {
            node.getProperties().putIfAbsent(BASE_TRANSLATE_X, node.getTranslateX());
            if (node.getStyleClass().contains("footer-background") && node instanceof javafx.scene.shape.Rectangle rectangle) {
                double rightInset = content.getPrefWidth() - rectangle.getLayoutX() - rectangle.getWidth();
                rectangle.getProperties().putIfAbsent(BASE_FOOTER_RIGHT, rightInset);
            }
        }

        Runnable updateLayout = () -> {
            if (content.getWidth() <= 0) {
                return;
            }
            double newWidth = content.getWidth();
            double horizontalGrowth = newWidth - DESIGN_WIDTH;
            for (Node node : content.getChildren()) {
                if (responsivePanels.contains(node) || isHorizontallyAnchored(node)) {
                    continue;
                }

                double baseTranslateX = (double) node.getProperties().get(BASE_TRANSLATE_X);
                if (node.getStyleClass().contains("footer-background")) {
                    node.setTranslateX(baseTranslateX);
                } else if ("themeToggleButton".equals(node.getId())) {
                    node.setTranslateX(baseTranslateX);
                } else if ("hboxCuenta".equals(node.getId()) || "vboxSearch".equals(node.getId())) {
                    node.setTranslateX(baseTranslateX + horizontalGrowth);
                } else {
                    node.setTranslateX(baseTranslateX + horizontalGrowth / 2);
                }

                if (node instanceof javafx.scene.shape.Rectangle rectangle
                        && node.getStyleClass().contains("footer-background")) {
                    double rightInset = (double) node.getProperties().get(BASE_FOOTER_RIGHT);
                    rectangle.setWidth(Math.max(0, newWidth - rectangle.getLayoutX() - rightInset));
                }
            }
        };
        content.widthProperty().addListener((observable, oldWidth, newWidth) -> updateLayout.run());
        Platform.runLater(updateLayout);

    }

    private static Node directChildById(AnchorPane parent, String id) {
        return parent.getChildren().stream()
                .filter(node -> id.equals(node.getId()))
                .findFirst()
                .orElse(null);
    }

    private static boolean isHorizontallyAnchored(Node node) {
        return AnchorPane.getLeftAnchor(node) != null || AnchorPane.getRightAnchor(node) != null;
    }

    private static void centerFixedSplashContent(AnchorPane screen) {
        for (Node node : screen.getChildren()) {
            node.getProperties().putIfAbsent(BASE_TRANSLATE_X, node.getTranslateX());
        }
        screen.widthProperty().addListener((observable, oldWidth, newWidth) -> {
            double offset = (newWidth.doubleValue() - DESIGN_WIDTH) / 2;
            for (Node node : screen.getChildren()) {
                if (!isHorizontallyAnchored(node)) {
                    node.setTranslateX((double) node.getProperties().get(BASE_TRANSLATE_X) + offset);
                }
            }
        });
    }
}
