
package thesweetspot.patterns.adapter;

import javafx.scene.image.Image;

public class URLImageAdapter implements productImage {
    private URLImage urlImage;

    public URLImageAdapter(URLImage urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    public Image loadImage(String source) {
        return urlImage.fetchFromURL(source);
    }
}
