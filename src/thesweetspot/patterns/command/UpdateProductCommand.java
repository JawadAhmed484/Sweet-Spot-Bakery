
package thesweetspot.patterns.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import thesweetspot.DialogUtils;
import thesweetspot.Data.data;
import thesweetspot.patterns.singleton.DBConnection;

public class UpdateProductCommand implements command {

    private final TextField productIDField;
    private final TextField productNameField;
    private final ComboBox<String> typeBox;
    private final TextField stockField;
    private final TextField priceField;
    private final ComboBox<String> statusBox;
    private final Runnable refreshTable;
    private final Runnable clearForm;

    public UpdateProductCommand(
            TextField productIDField,
            TextField productNameField,
            ComboBox<?> typeBox,
            TextField stockField,
            TextField priceField,
            ComboBox<?> statusBox,
            Runnable refreshTable,
            Runnable clearForm
    ) {
        this.productIDField = productIDField;
        this.productNameField = productNameField;
        this.typeBox = (ComboBox<String>) typeBox;
        this.stockField = stockField;
        this.priceField = priceField;
        this.statusBox = (ComboBox<String>) statusBox;
        this.refreshTable = refreshTable;
        this.clearForm = clearForm;
    }

    @Override
    public void execute() {
        try {
            if (productIDField.getText().isEmpty()
                    || productNameField.getText().isEmpty()
                    || typeBox.getSelectionModel().getSelectedItem() == null
                    || stockField.getText().isEmpty()
                    || priceField.getText().isEmpty()
                    || statusBox.getSelectionModel().getSelectedItem() == null
                    || data.path == null || data.id == 0) {

                showAlert("Please fill all blank fields", Alert.AlertType.ERROR);
                return;
            }

            String path = data.path.replace("\\", "\\\\");

            String updateData = "UPDATE products SET "
                    + "Product_ID = ?, Product_Name = ?, Type = ?, Stock = ?, Price = ?, Status = ?, Image = ?, Date = ? "
                    + "WHERE ID = ?";

            Connection connect = DBConnection.getInstance();
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation Message");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to UPDATE Product ID: " + productIDField.getText() + "?");
            DialogUtils.applyBakeryIcon(confirm); 
            Optional<ButtonType> option = confirm.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                PreparedStatement prepare = connect.prepareStatement(updateData);
                prepare.setString(1, productIDField.getText());
                prepare.setString(2, productNameField.getText());
                prepare.setString(3, typeBox.getSelectionModel().getSelectedItem());
                prepare.setString(4, stockField.getText());
                prepare.setString(5, priceField.getText());
                prepare.setString(6, statusBox.getSelectionModel().getSelectedItem());
                prepare.setString(7, path);
                prepare.setString(8, data.date); // assumed to be already formatted
                prepare.setInt(9, data.id);

                prepare.executeUpdate();

                showAlert("Successfully Updated!", Alert.AlertType.INFORMATION);

                if (refreshTable != null) {
                    refreshTable.run();
                }
                if (clearForm != null) {
                    clearForm.run();
                }

            } else {
                showAlert("Update Cancelled.", Alert.AlertType.INFORMATION);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to update product: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Product Update");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        DialogUtils.applyBakeryIcon(alert); 
        alert.showAndWait();
    }
}