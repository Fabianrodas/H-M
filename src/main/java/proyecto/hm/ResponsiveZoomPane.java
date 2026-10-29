package proyecto.hm;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;

/** Scales an FXML design uniformly as its viewport grows, like browser page zoom. */
final class ResponsiveZoomPane extends Region {

    private final Region content;
    private final double designWidth;
    private final Scale contentScale = new Scale(1, 1, 0, 0);

    ResponsiveZoomPane(Region content, double designWidth) {
        if (content == null || designWidth <= 0) {
            throw new IllegalArgumentException("Content and a positive design width are required.");
        }
        this.content = content;
        this.designWidth = designWidth;

        content.setMinSize(0, 0);
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        content.getTransforms().add(contentScale);
        enableSmoothImageScaling(content);
        getChildren().add(content);

        setMinSize(0, 0);
        setPrefWidth(designWidth);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        widthProperty().addListener((observable, oldWidth, newWidth) -> {
            requestLayout();
            if (getParent() != null) {
                getParent().requestLayout();
            }
        });
    }

    @Override
    protected void layoutChildren() {
        double zoom = scaleForWidth(getWidth());
        double designHeight = contentDesignHeight();

        content.resize(designWidth, Math.max(0, designHeight));
        content.relocate(0, 0);
        content.layout();

        double laidOutHeight = contentDesignHeight();
        if (laidOutHeight > designHeight + 0.5) {
            content.resize(designWidth, laidOutHeight);
            content.layout();
            requestLayout();
        }

        contentScale.setX(zoom);
        contentScale.setY(zoom);
    }

    @Override
    protected double computeMinWidth(double height) {
        return 0;
    }

    @Override
    protected double computeMinHeight(double width) {
        return 0;
    }

    @Override
    protected double computePrefWidth(double height) {
        return designWidth;
    }

    @Override
    protected double computePrefHeight(double width) {
        double effectiveWidth = getWidth() > 0 ? getWidth() : width;
        return contentDesignHeight() * scaleForWidth(effectiveWidth);
    }

    private double contentDesignHeight() {
        double preferredHeight = content.prefHeight(designWidth);
        if (!Double.isFinite(preferredHeight) || preferredHeight <= 0) {
            preferredHeight = content.getHeight();
        }

        double contentHeight = Math.max(0, preferredHeight);
        if (content instanceof Parent parent) {
            contentHeight = Math.max(contentHeight, visibleDescendantBottom(parent));
        }
        return contentHeight;
    }

    private double visibleDescendantBottom(Parent parent) {
        double contentBottom = 0;
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (!child.isVisible() || child.getOpacity() <= 0) {
                continue;
            }

            Bounds bounds = boundsInContent(child);
            boolean hasVisibleChildren = child instanceof Parent childParent
                    && childParent.getChildrenUnmodifiable().stream()
                            .anyMatch(grandchild -> grandchild.isVisible() && grandchild.getOpacity() > 0);
            if (bounds != null && (!hasVisibleChildren
                    || child instanceof Region region && hasVisibleSurface(region))
                    && Double.isFinite(bounds.getMaxY())) {
                contentBottom = Math.max(contentBottom, bounds.getMaxY());
            }

            if (child instanceof Parent childParent) {
                contentBottom = Math.max(contentBottom, visibleDescendantBottom(childParent));
            }
        }
        return contentBottom;
    }

    private Bounds boundsInContent(Node node) {
        Bounds bounds = node.getBoundsInLocal();
        Node current = node;
        while (current != content) {
            bounds = current.localToParent(bounds);
            current = current.getParent();
            if (current == null) {
                return null;
            }
        }
        return bounds;
    }

    private static boolean hasVisibleSurface(Region region) {
        Background background = region.getBackground();
        if (background != null) {
            if (!background.getImages().isEmpty()
                    || background.getFills().stream().anyMatch(fill -> isVisiblePaint(fill.getFill()))) {
                return true;
            }
        }

        Border border = region.getBorder();
        if (border == null) {
            return false;
        }
        if (!border.getImages().isEmpty()) {
            return true;
        }
        return border.getStrokes().stream().anyMatch(ResponsiveZoomPane::hasVisibleStroke);
    }

    private static boolean hasVisibleStroke(BorderStroke stroke) {
        return isVisiblePaint(stroke.getTopStroke())
                || isVisiblePaint(stroke.getRightStroke())
                || isVisiblePaint(stroke.getBottomStroke())
                || isVisiblePaint(stroke.getLeftStroke());
    }

    private static boolean isVisiblePaint(Paint paint) {
        return paint != null && (!(paint instanceof Color color) || color.getOpacity() > 0);
    }

    private double scaleForWidth(double width) {
        return width > 0 ? width / designWidth : 1;
    }

    private static void enableSmoothImageScaling(Parent parent) {
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (child instanceof ImageView imageView) {
                imageView.setSmooth(true);
            } else if (child instanceof Parent nested) {
                enableSmoothImageScaling(nested);
            }
        }
    }
}
