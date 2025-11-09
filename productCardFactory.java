package thesweetspot;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import thesweetspot.Data.productData;

public class productCardFactory {
    public AnchorPane createProductCard(productData data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cardProduct.fxml"));
            AnchorPane pane = loader.load();
            CardProductController controller = loader.getController();
            controller.setData(data);
            return pane;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
