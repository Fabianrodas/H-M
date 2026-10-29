package proyecto.hm;

import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

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
}
