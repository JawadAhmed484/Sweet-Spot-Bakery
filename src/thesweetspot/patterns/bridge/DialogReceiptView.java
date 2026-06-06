package thesweetspot.patterns.bridge;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import thesweetspot.DialogUtils;

public class DialogReceiptView implements receiptView {

    public DialogReceiptView() {
    }

    @Override
    public void showReceipt(String receiptText) {
        try {
            Dialog<ButtonType> receiptDialog = new Dialog<>();
            receiptDialog.setTitle("Receipt");
            receiptDialog.setHeaderText("Transaction Receipt");

            ImageView logo = new ImageView(new Image(getClass().getResource("/resources/Bakery(Main Form).jpg").toExternalForm()));
            logo.setFitHeight(60);
            logo.setFitWidth(60);
            receiptDialog.setGraphic(logo);

            TextArea textArea = new TextArea(receiptText);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
            textArea.setPrefHeight(400);
            textArea.setPrefRowCount(25);
            textArea.setPrefColumnCount(45);
            textArea.setMinHeight(Region.USE_PREF_SIZE);

            receiptDialog.getDialogPane().setContent(textArea);
            receiptDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);


            DialogUtils.applyBakeryIcon(receiptDialog);
            receiptDialog.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not display receipt.\n" + e.getMessage());
            DialogUtils.applyBakeryIcon(alert);
            alert.showAndWait();
        }
    }

}
