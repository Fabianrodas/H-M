package proyecto.hm;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;
import javafx.geometry.Bounds;

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
            for (Node child : parent.getChildrenUnmodifiable()) {
                if (!child.isVisible()) {
                    continue;
                }

                Bounds bounds = child.getBoundsInParent();
                if (Double.isFinite(bounds.getMaxY())) {
                    contentHeight = Math.max(contentHeight, bounds.getMaxY());
                }

                double childHeight = child.getLayoutBounds().getHeight();
                if (child instanceof Region region) {
                    double preferredChildHeight = region.prefHeight(-1);
                    if (Double.isFinite(preferredChildHeight) && preferredChildHeight > 0) {
                        childHeight = Math.max(childHeight, preferredChildHeight);
                    }
                }

                double childBottom = child.getLayoutY() + child.getTranslateY() + childHeight;
                if (Double.isFinite(childBottom)) {
                    contentHeight = Math.max(contentHeight, childBottom);
                }
            }
        }
        return contentHeight;
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
