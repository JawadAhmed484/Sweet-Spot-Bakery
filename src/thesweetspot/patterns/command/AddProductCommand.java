
package thesweetspot.patterns.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import thesweetspot.DialogUtils;
import thesweetspot.Data.data;
import thesweetspot.patterns.singleton.DBConnection;

public class AddProductCommand implements command {

    private final TextField productIDField;
    private final TextField productNameField;
    private final ComboBox<String> typeBox;
    private final TextField stockField;
    private final TextField priceField;
    private final ComboBox<String> statusBox;
    private final Runnable refreshTable;
    private final Runnable clearForm;

    public AddProductCommand(
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
                    || data.path == null) {

                showAlert("Please fill all blank fields", Alert.AlertType.ERROR);
                return;
            }

            Connection connect = DBConnection.getInstance();

            String checkProdID = "SELECT Product_ID FROM products WHERE Product_ID = ?";
            PreparedStatement checkStmt = connect.prepareStatement(checkProdID);
            checkStmt.setString(1, productIDField.getText());
            ResultSet result = checkStmt.executeQuery();

            if (result.next()) {
                showAlert(productIDField.getText() + " is already taken", Alert.AlertType.ERROR);
                return;
            }

            String insertData = "INSERT INTO products "
                    + "(Product_ID, Product_Name, Type, Stock, Price, Status, Image, Date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement prepare = connect.prepareStatement(insertData);
            prepare.setString(1, productIDField.getText());
            prepare.setString(2, productNameField.getText());
            prepare.setString(3, typeBox.getSelectionModel().getSelectedItem());
            prepare.setString(4, stockField.getText());
            prepare.setString(5, priceField.getText());
            prepare.setString(6, statusBox.getSelectionModel().getSelectedItem());

            String path = data.path.replace("\\", "\\\\");
            prepare.setString(7, path);

            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            prepare.setString(8, String.valueOf(sqlDate));

            prepare.executeUpdate();

            showAlert("Successfully Added!", Alert.AlertType.INFORMATION);

            if (refreshTable != null) {
                refreshTable.run();
            }
            if (clearForm != null) {
                clearForm.run();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to add product: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Add Product");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogUtils.applyBakeryIcon(alert); 
        alert.showAndWait();
    }
}