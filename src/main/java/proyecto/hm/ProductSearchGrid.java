package proyecto.hm;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.DoubleConsumer;

/** Keeps search results in their original four-column design before the whole page is zoomed. */
final class ProductSearchGrid {

    private static final double DESIGN_WIDTH = 688;
    private static final double CARD_WIDTH = 150;
    private static final double HORIZONTAL_GAP = 10;
    private static final double VERTICAL_GAP = 20;

    private ProductSearchGrid() {
    }

    static FlowPane create(List<VBox> cards, DoubleConsumer heightChangeHandler) {
        FlowPane grid = new FlowPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setRowValignment(VPos.TOP);
        grid.setHgap(HORIZONTAL_GAP);
        grid.setVgap(VERTICAL_GAP);
        grid.setPrefWrapLength(DESIGN_WIDTH);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);
        grid.getStyleClass().add("responsive-product-grid");

        for (VBox card : cards) {
            card.setMinWidth(0);
            card.setPrefWidth(CARD_WIDTH);
            card.setMaxWidth(CARD_WIDTH);
        }
        grid.getChildren().setAll(cards);

        Runnable updateHeight = () -> {
            double width = grid.getWidth() > 0 ? grid.getWidth() : DESIGN_WIDTH;
            if (heightChangeHandler != null) {
                heightChangeHandler.accept(grid.prefHeight(width));
            }
        };
        grid.widthProperty().addListener((observable, oldWidth, newWidth) -> updateHeight.run());
        grid.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(updateHeight);
            }
        });
        Platform.runLater(updateHeight);
        return grid;
    }
}
