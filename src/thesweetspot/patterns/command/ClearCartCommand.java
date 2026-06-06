
package thesweetspot.patterns.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import thesweetspot.DialogUtils;
import thesweetspot.Data.data;
import thesweetspot.patterns.observer.dashboardSubject;
import thesweetspot.patterns.singleton.DBConnection;

public class ClearCartCommand implements command {

   private final Runnable refreshUI;
    private final Label totalLabel;
    private final Label changeLabel;

    public ClearCartCommand(Runnable refreshUI, Label totalLabel, Label changeLabel) {
        this.refreshUI = refreshUI;
        this.totalLabel = totalLabel;
        this.changeLabel = changeLabel;
    }

    @Override
    public void execute() {
        try {
            if (data.cID == null) {
                showAlert("Cart is already empty", Alert.AlertType.ERROR);
                return;
            }

            // Confirmation Dialog
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to clear the entire cart?");
            DialogUtils.applyBakeryIcon(confirm); 
            Optional<ButtonType> option = confirm.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                // Clear cart
                Connection connect = DBConnection.getInstance();
                String sql = "DELETE FROM customer WHERE customer_id = ?";
                PreparedStatement prepare = connect.prepareStatement(sql);
                prepare.setInt(1, data.cID);
                prepare.executeUpdate();

                data.cID = null;

                // UI Reset
                if (refreshUI != null) {
                    refreshUI.run(); // e.g., controller::menuShowOrderData
                }

                if (totalLabel != null) {
                    totalLabel.setText("$0.00");
                }
                if (changeLabel != null) {
                    changeLabel.setText("$0.00");
                }

                showAlert("Cart cleared successfully", Alert.AlertType.INFORMATION);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error clearing cart", Alert.AlertType.ERROR);
        }
        dashboardSubject.notifyObservers();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Cart");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogUtils.applyBakeryIcon(alert); 
        alert.showAndWait();
    }

}
