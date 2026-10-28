package proyecto.hm;

import java.net.URL;
import javafx.scene.image.Image;

final class ImageResources {

    private ImageResources() {
    }

    static Image load(String resourcePath) {
        URL imageUrl = ImageResources.class.getResource(resourcePath);
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: " + resourcePath);
        }
        return new Image(imageUrl.toExternalForm());
    }
}
