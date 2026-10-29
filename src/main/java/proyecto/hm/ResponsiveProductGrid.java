package proyecto.hm;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleConsumer;

/** Builds product grids whose cards grow to a sharp, source-sized maximum before wrapping. */
final class ResponsiveProductGrid {

    private static final double CATALOG_CARD_MAX_WIDTH = 260;
    private static final double SEARCH_CARD_MAX_WIDTH = 220;
    private static final double SEARCH_CARD_GAP = 14;
    private static final double FONT_SCALE_REFERENCE_WIDTH = 192;
    private static final String BASE_FONT = "responsive-grid-base-font";

    private ResponsiveProductGrid() {
    }

    static List<Node> installCatalogGrids(AnchorPane content) {
        List<Node> responsivePanels = new ArrayList<>();

        for (Node node : new ArrayList<>(content.getChildren())) {
            if (!(node instanceof VBox panel)) {
                continue;
            }

            List<HBox> productRows = new ArrayList<>();
            List<VBox> cards = new ArrayList<>();
            double originalGridHeight = 0;

            for (Node child : panel.getChildren()) {
                if (!(child instanceof HBox row)) {
                    continue;
                }

                List<VBox> rowCards = new ArrayList<>();
                for (Node rowChild : row.getChildren()) {
                    if (rowChild instanceof VBox card && containsImage(card)) {
                        rowCards.add(card);
                    }
                }

                if (rowCards.size() < 2) {
                    continue;
                }
                productRows.add(row);
                cards.addAll(rowCards);
                originalGridHeight += preferredHeight(row);
            }

            if (productRows.isEmpty()) {
                continue;
            }

            double originalX = panel.getLayoutX();
            double originalWidth = preferredWidth(panel);
            double contentWidth = preferredWidth(content);
            double rightInset = Math.max(0, contentWidth - originalX - originalWidth);
            double originalContentHeight = preferredHeight(content);

            FlowPane grid = new FlowPane();
            grid.setAlignment(Pos.TOP_CENTER);
            grid.setRowValignment(VPos.TOP);
            grid.setHgap(0);
            grid.setVgap(12);
            grid.setMinWidth(0);
            grid.setMaxWidth(Double.MAX_VALUE);
            grid.setPrefWidth(originalWidth);
            grid.getStyleClass().add("responsive-product-grid");
            grid.getChildren().setAll(cards);

            panel.getChildren().setAll(grid);
            panel.setMinWidth(0);
            panel.setMaxWidth(Double.MAX_VALUE);
            panel.setPrefWidth(originalWidth);
            AnchorPane.setLeftAnchor(panel, originalX);
            AnchorPane.setRightAnchor(panel, rightInset);
            responsivePanels.add(panel);

            double baseGridHeight = originalGridHeight;
            double footerTop = findFooterTop(content, panel.getLayoutY() + originalGridHeight);
            List<Node> footerNodes = footerNodes(content, footerTop);
            List<Double> footerTranslateY = footerNodes.stream()
                    .map(Node::getTranslateY)
                    .toList();

            makeResponsive(grid, cards, 3, CATALOG_CARD_MAX_WIDTH, 0, width -> {
                double newGridHeight = grid.prefHeight(width);
                double heightChange = newGridHeight - baseGridHeight;
                panel.setPrefHeight(newGridHeight);
                for (int i = 0; i < footerNodes.size(); i++) {
                    footerNodes.get(i).setTranslateY(footerTranslateY.get(i) + heightChange);
                }
                content.setPrefHeight(Math.max(450, originalContentHeight + heightChange));
            });
        }
        return responsivePanels;
    }

    static FlowPane createSearchGrid(List<VBox> cards, DoubleConsumer heightChangeHandler) {
        FlowPane grid = new FlowPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setRowValignment(VPos.TOP);
        grid.setHgap(SEARCH_CARD_GAP);
        grid.setVgap(20);
        grid.setMinWidth(0);
        grid.setMaxWidth(Double.MAX_VALUE);
        grid.getStyleClass().add("responsive-product-grid");
        grid.getChildren().setAll(cards);
        makeResponsive(grid, cards, 4, SEARCH_CARD_MAX_WIDTH, SEARCH_CARD_GAP, heightChangeHandler);
        return grid;
    }

    private static void makeResponsive(FlowPane grid, List<VBox> cards, int initialColumns,
                                       double maxCardWidth, double horizontalGap,
                                       DoubleConsumer heightChangeHandler) {
        Runnable update = () -> {
            double width = grid.getWidth();
            if (width <= 0) {
                width = grid.getPrefWidth();
            }
            if (width <= 0 || cards.isEmpty()) {
                return;
            }

            int columnsAtMaxSize = (int) Math.floor((width + horizontalGap) / (maxCardWidth + horizontalGap));
            int columnCount = Math.min(cards.size(), Math.max(initialColumns, columnsAtMaxSize));
            double cardWidth = (width - horizontalGap * (columnCount - 1)) / columnCount;
            cardWidth = Math.min(maxCardWidth, cardWidth);

            for (VBox card : cards) {
                double textScale = Math.min(1.25, Math.max(1, cardWidth / FONT_SCALE_REFERENCE_WIDTH));
                resizeCard(card, cardWidth, textScale);
            }
            grid.setPrefWrapLength(width);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.requestLayout();
            if (heightChangeHandler != null) {
                double measuredWidth = width;
                Platform.runLater(() -> heightChangeHandler.accept(grid.prefHeight(measuredWidth)));
            }
        };

        grid.widthProperty().addListener((observable, oldWidth, newWidth) -> update.run());
        grid.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(update);
            }
        });
        Platform.runLater(update);
    }

    private static void resizeCard(VBox card, double cardWidth, double textScale) {
        card.setMinWidth(0);
        card.setPrefWidth(cardWidth);
        card.setMaxWidth(cardWidth);

        for (Node node : card.getChildren()) {
            resizeCardContent(node, cardWidth, textScale);
        }
    }

    private static void resizeCardContent(Node node, double cardWidth, double textScale) {
        if (node instanceof ImageView imageView) {
            Image image = imageView.getImage();
            if (image != null && image.getWidth() > 0 && image.getHeight() > 0) {
                double imageWidth = Math.min(cardWidth, image.getWidth());
                imageView.setFitWidth(imageWidth);
                imageView.setFitHeight(imageWidth * image.getHeight() / image.getWidth());
            }
            return;
        }

        if (node instanceof Region region) {
            region.setMinWidth(0);
            region.setPrefWidth(cardWidth);
            region.setMaxWidth(cardWidth);
        }
        if (node instanceof Label label) {
            label.setWrapText(true);
            Font baseFont = (Font) label.getProperties().computeIfAbsent(BASE_FONT, key -> label.getFont());
            String fontStyle = (baseFont.getStyle() + " " + label.getStyle()).toLowerCase();
            FontWeight weight = fontStyle.contains("bold") ? FontWeight.BOLD : FontWeight.NORMAL;
            FontPosture posture = fontStyle.contains("italic") ? FontPosture.ITALIC : FontPosture.REGULAR;
            label.setFont(Font.font(baseFont.getFamily(), weight, posture, baseFont.getSize() * textScale));
        }
        if (node instanceof javafx.scene.Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                resizeCardContent(child, cardWidth, textScale);
            }
        }
    }

    private static boolean containsImage(VBox card) {
        return card.getChildrenUnmodifiable().stream()
                .anyMatch(ResponsiveProductGrid::containsImage);
    }

    private static boolean containsImage(Node node) {
        if (node instanceof ImageView) {
            return true;
        }
        if (node instanceof javafx.scene.Parent parent) {
            return parent.getChildrenUnmodifiable().stream()
                    .anyMatch(ResponsiveProductGrid::containsImage);
        }
        return false;
    }

    private static List<Node> footerNodes(AnchorPane content, double footerTop) {
        List<Node> footer = new ArrayList<>();
        for (Node node : content.getChildren()) {
            double y = node.getLayoutY() + node.getTranslateY();
            if (isFooterNode(node) || y >= footerTop - 1) {
                footer.add(node);
            }
        }
        return footer;
    }

    private static double findFooterTop(AnchorPane content, double fallback) {
        return content.getChildren().stream()
                .filter(node -> node.getStyleClass().contains("footer-background"))
                .mapToDouble(node -> node.getLayoutY() + node.getTranslateY())
                .min()
                .orElse(fallback);
    }

    private static boolean isFooterNode(Node node) {
        if (node.getStyleClass().contains("footer-background")
                || node.getStyleClass().contains("footer-copyright")) {
            return true;
        }
        return "hboxPie".equals(node.getId()) || "imagenPie".equals(node.getId());
    }

    private static double preferredWidth(Region region) {
        double preferred = region.getPrefWidth();
        return Double.isFinite(preferred) && preferred > 0 ? preferred : region.prefWidth(-1);
    }

    private static double preferredHeight(Region region) {
        double preferred = region.getPrefHeight();
        return Double.isFinite(preferred) && preferred > 0 ? preferred : region.prefHeight(-1);
    }
}
