package thesweetspot;

import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DialogUtils {
    public static void applyBakeryIcon(Dialog<?> dialog) {
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        Image icon = new Image(DialogUtils.class.getResource("/resources/Bakery(Main Form).jpg").toExternalForm());
        stage.getIcons().add(icon);
    }
}
