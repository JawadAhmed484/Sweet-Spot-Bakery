
package thesweetspot.patterns.adapter;

import javafx.scene.image.Image;

public class URLImage {
    public Image fetchFromURL(String url) {
        return new Image(url, 190, 94, false, true);
    }
}
