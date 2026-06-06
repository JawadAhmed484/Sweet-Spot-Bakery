
package thesweetspot.patterns.adapter;

import javafx.scene.image.Image;

public class FileImageLoader implements productImage {

    @Override
    public Image loadImage(String source) {
        return new Image("File:" + source, 190, 94, false, true);
    }
}
