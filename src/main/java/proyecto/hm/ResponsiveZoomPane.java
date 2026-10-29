package proyecto.hm;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
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
    }

    @Override
    protected void layoutChildren() {
        double zoom = scaleForWidth(getWidth());
        double designHeight = content.prefHeight(designWidth);
        if (!Double.isFinite(designHeight) || designHeight <= 0) {
            designHeight = content.getHeight();
        }

        content.resize(designWidth, Math.max(0, designHeight));
        content.relocate(0, 0);
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
        double designHeight = content.prefHeight(designWidth);
        if (!Double.isFinite(designHeight) || designHeight <= 0) {
            designHeight = content.getHeight();
        }
        return Math.max(0, designHeight) * scaleForWidth(width);
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
