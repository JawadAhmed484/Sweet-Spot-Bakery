
package thesweetspot.patterns.command;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import thesweetspot.Data.data;

public class ClearFormCommand implements command {

   private final TextField productIDField;
    private final TextField productNameField;
    private final ComboBox<?> typeBox;
    private final TextField stockField;
    private final TextField priceField;
    private final ComboBox<?> statusBox;
    private final ImageView imageView;

    public ClearFormCommand(
            TextField productIDField,
            TextField productNameField,
            ComboBox<?> typeBox,
            TextField stockField,
            TextField priceField,
            ComboBox<?> statusBox,
            ImageView imageView
    ) {
        this.productIDField = productIDField;
        this.productNameField = productNameField;
        this.typeBox = typeBox;
        this.stockField = stockField;
        this.priceField = priceField;
        this.statusBox = statusBox;
        this.imageView = imageView;
    }

    @Override
    public void execute() {
        productIDField.setText("");
        productNameField.setText("");
        typeBox.getSelectionModel().clearSelection();
        stockField.setText("");
        priceField.setText("");
        statusBox.getSelectionModel().clearSelection();
        data.path = "";
        data.id = 0;
        imageView.setImage(null);
    }
}