package proyecto.hm;

import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

/** Keeps a fixed-design screen proportional while the application window is resized. */
final class ResponsiveScalePane extends Region {

    private final double designWidth;
    private final double designHeight;
    private final double contentWidth;
    private final double contentHeight;
    private final Rectangle viewportClip = new Rectangle();
    private final Scale contentScale = new Scale(1, 1, 0, 0);

    private Region content;

    ResponsiveScalePane(Region content, double designWidth, double designHeight) {
        if (designWidth <= 0 || designHeight <= 0) {
            throw new IllegalArgumentException("Design dimensions must be positive.");
        }

        this.designWidth = designWidth;
        this.designHeight = designHeight;
        this.contentWidth = preferredDimension(content.prefWidth(-1), designWidth);
        this.contentHeight = preferredDimension(content.prefHeight(-1), designHeight);

        setMinSize(0, 0);
        setPrefSize(designWidth, designHeight);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setClip(viewportClip);
        setContent(content);
    }

    Region getContent() {
        return content;
    }

    void setContent(Region nextContent) {
        if (content != null) {
            content.getTransforms().remove(contentScale);
        }

        content = nextContent;
        content.getTransforms().add(contentScale);
        getChildren().setAll(content);
        requestLayout();
    }

    @Override
    protected void layoutChildren() {
        double width = getWidth();
        double height = getHeight();
        viewportClip.setWidth(width);
        viewportClip.setHeight(height);

        if (width <= 0 || height <= 0) {
            return;
        }

        double scale = Math.min(width / designWidth, height / designHeight);
        double scaledViewportWidth = designWidth * scale;
        double scaledViewportHeight = designHeight * scale;
        double offsetX = (width - scaledViewportWidth) / 2;
        double offsetY = (height - scaledViewportHeight) / 2;

        content.resize(contentWidth, contentHeight);
        contentScale.setX(scale);
        contentScale.setY(scale);
        content.setLayoutX(offsetX);
        content.setLayoutY(offsetY);
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
        return designHeight;
    }

    private static double preferredDimension(double preferred, double fallback) {
        return Double.isFinite(preferred) && preferred > 0 ? preferred : fallback;
    }
}
