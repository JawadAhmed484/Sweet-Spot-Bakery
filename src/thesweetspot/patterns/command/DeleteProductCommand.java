
package thesweetspot.patterns.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import thesweetspot.DialogUtils;
import thesweetspot.Data.data;
import thesweetspot.patterns.singleton.DBConnection;

public class DeleteProductCommand implements command {

     private final TextField productIDField;
    private final Runnable refreshTable;
    private final Runnable clearForm;

    public DeleteProductCommand(
            TextField productIDField,
            Runnable refreshTable,
            Runnable clearForm
    ) {
        this.productIDField = productIDField;
        this.refreshTable = refreshTable;
        this.clearForm = clearForm;
    }

    @Override
    public void execute() {
        Alert alert;
        if (data.id == 0) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to delete");
            DialogUtils.applyBakeryIcon(alert); 
            alert.showAndWait();
            return;
        }

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to DELETE Product ID: " + productIDField.getText() + "?");
        DialogUtils.applyBakeryIcon(alert); 
        Optional<ButtonType> option = alert.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            try {
                Connection connect = DBConnection.getInstance();
                String deleteData = "DELETE FROM products WHERE id = " + data.id;
                PreparedStatement prepare = connect.prepareStatement(deleteData);
                prepare.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Deleted!");
                DialogUtils.applyBakeryIcon(alert); 
                alert.showAndWait();

                if (refreshTable != null) {
                    refreshTable.run();
                }
                if (clearForm != null) {
                    clearForm.run();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Cancelled");
            DialogUtils.applyBakeryIcon(alert); 
            alert.showAndWait();
        }
    }
}