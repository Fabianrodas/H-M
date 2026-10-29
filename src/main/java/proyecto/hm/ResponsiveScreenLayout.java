package proyecto.hm;

import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.control.Tooltip;

/** Installs uniform browser-style zoom while preserving each FXML screen's original proportions. */
final class ResponsiveScreenLayout {

    private static final double DESIGN_WIDTH = 750;

    private ResponsiveScreenLayout() {
    }

    static Parent prepare(Parent root) {
        if (!(root instanceof AnchorPane screen)) {
            return root;
        }

        screen.setMinWidth(0);
        screen.setMaxWidth(Double.MAX_VALUE);
        screen.setMinHeight(0);
        screen.setMaxHeight(Double.MAX_VALUE);
        ScrollPane scrollPane = screen.getChildrenUnmodifiable().stream()
                .filter(ScrollPane.class::isInstance)
                .map(ScrollPane.class::cast)
                .findFirst()
                .orElse(null);
        installWindowSizeButton(screen, scrollPane);

        if (scrollPane == null) {
            return new ResponsiveZoomPane(screen, DESIGN_WIDTH);
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

        if (scrollPane.getContent() instanceof Region content) {
            scrollPane.setContent(null);
            scrollPane.setContent(new ResponsiveZoomPane(content, DESIGN_WIDTH));
        }
        return screen;
    }

    private static void installWindowSizeButton(AnchorPane screen, ScrollPane scrollPane) {
        Parent searchRoot = scrollPane != null && scrollPane.getContent() instanceof Parent content
                ? content
                : screen;
        Node themeButtonNode = findNodeById(searchRoot, "themeToggleButton");
        if (!(themeButtonNode instanceof Button themeButton)
                || !(themeButton.getParent() instanceof Pane parent)
                || findNodeById(parent, "resetWindowSizeButton") != null) {
            return;
        }

        Button resetButton = new Button("Original size");
        resetButton.setId("resetWindowSizeButton");
        resetButton.setMnemonicParsing(false);
        resetButton.setAccessibleText("Restore original window size");
        resetButton.setTooltip(new Tooltip("Restore original window size"));
        resetButton.getStyleClass().add("reset-size-button");
        resetButton.setPrefWidth(90);
        resetButton.setMinWidth(90);
        resetButton.setMaxWidth(90);
        resetButton.setPrefHeight(themeButton.prefHeight(-1));

        double themeButtonWidth = themeButton.prefWidth(-1);
        if (!Double.isFinite(themeButtonWidth) || themeButtonWidth <= 0) {
            themeButtonWidth = 92;
        }
        resetButton.setLayoutX(themeButton.getLayoutX() + themeButtonWidth + 5);
        resetButton.setLayoutY(themeButton.getLayoutY());
        resetButton.setOnAction(event -> App.restoreOriginalWindowSize());
        parent.getChildren().add(resetButton);
    }

    private static Node findNodeById(Parent parent, String id) {
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (id.equals(child.getId())) {
                return child;
            }
            if (child instanceof Parent nested) {
                Node match = findNodeById(nested, id);
                if (match != null) {
                    return match;
                }
            }
        }
        return null;
    }
}
