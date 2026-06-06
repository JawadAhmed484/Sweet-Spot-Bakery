
package thesweetspot.patterns.command;

import java.io.File;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import thesweetspot.DialogUtils;
import thesweetspot.Data.data;

public class ImportImageCommand implements command {

    private final ImageView imageView;
    private final Window parentWindow;

    public ImportImageCommand(ImageView imageView, Window parentWindow) {
        this.imageView = imageView;
        this.parentWindow = parentWindow;
    }

    @Override
    public void execute() {
        Alert choiceAlert = new Alert(Alert.AlertType.CONFIRMATION);
        choiceAlert.setTitle("Select Image Source");
        choiceAlert.setHeaderText(null);
        DialogUtils.applyBakeryIcon(choiceAlert); 
        choiceAlert.setContentText("Would you like to enter an online image URL or select a local file?");

        ButtonType urlOption = new ButtonType("URL");
        ButtonType fileOption = new ButtonType("File");
        ButtonType cancelOption = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        choiceAlert.getButtonTypes().setAll(urlOption, fileOption, cancelOption);

        Optional<ButtonType> result = choiceAlert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == urlOption) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Image URL");
                dialog.setHeaderText(null);
                dialog.setContentText("Enter Image URL:");
                DialogUtils.applyBakeryIcon(dialog);

                Optional<String> urlResult = dialog.showAndWait();
                urlResult.ifPresent(url -> {
                    try {
                        Image image = new Image(url, 121, 126, false, true);
                        imageView.setImage(image);
                        data.path = url; // Store URL
                    } catch (Exception e) {
                        showError("Failed to load image from URL.");
                    }
                });

            } else if (result.get() == fileOption) {
                FileChooser openFile = new FileChooser();
                openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                File file = openFile.showOpenDialog(parentWindow);

                if (file != null) {
                    data.path = file.getAbsolutePath();
                    Image image = new Image(file.toURI().toString(), 121, 126, false, true);
                    imageView.setImage(image);
                }
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogUtils.applyBakeryIcon(alert); 
        alert.showAndWait();
    }
}
