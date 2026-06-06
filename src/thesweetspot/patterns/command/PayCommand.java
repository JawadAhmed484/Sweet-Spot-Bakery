
package thesweetspot.patterns.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import thesweetspot.DialogUtils;
import thesweetspot.Main_FormController;
import thesweetspot.Data.data;
import thesweetspot.patterns.observer.dashboardSubject;
import thesweetspot.patterns.singleton.DBConnection;

public class PayCommand implements command {

    private final TextField amountField;
    private final Label changeLabel;
    private final Label totalLabel;
    private final Button receiptButton;
    private final Runnable refreshTotal;
    private final Runnable generateReceipt;

    public PayCommand(
            TextField amountField,
            Label changeLabel,
            Label totalLabel,
            Button receiptButton,
            Runnable refreshTotal,
            Runnable generateReceipt
    ) {
        this.amountField = amountField;
        this.changeLabel = changeLabel;
        this.totalLabel = totalLabel;
        this.receiptButton = receiptButton;
        this.refreshTotal = refreshTotal;
        this.generateReceipt = generateReceipt;
    }

    @Override
    public void execute() {
        try {
            // Check if cart is empty
            double totalP = Double.parseDouble(totalLabel.getText().replace("$", "").trim());
            if (totalP == 0 || data.cID == null) {
                showAlert("Please choose your order first!", Alert.AlertType.ERROR);
                return;
            }

            // Check if amount is entered
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                showAlert("Please enter payment amount!", Alert.AlertType.ERROR);
                return;
            }

            double amountReceived;
            try {
                amountReceived = Double.parseDouble(amountText);
            } catch (NumberFormatException ex) {
                showAlert("Please enter a valid payment amount!", Alert.AlertType.ERROR);
                return;
            }

            // Refresh total
            if (refreshTotal != null) {
                refreshTotal.run();
            }
            totalP = Double.parseDouble(totalLabel.getText().replace("$", "").trim());

            if (amountReceived < totalP) {
                showAlert("Insufficient payment amount!", Alert.AlertType.ERROR);
                return;
            }

            double change = amountReceived - totalP;
            changeLabel.setText("$" + String.format("%.2f", change));

            // Confirmation dialog
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation Message");
            confirm.setHeaderText(null);
            confirm.setContentText("Process payment of $" + String.format("%.2f", amountReceived)
                    + "?\nTotal: $" + String.format("%.2f", totalP)
                    + "\nChange: $" + String.format("%.2f", change));
            DialogUtils.applyBakeryIcon(confirm); 
            Optional<ButtonType> option = confirm.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {

                // Insert receipt
                Connection connect = DBConnection.getInstance();
                String insertReceipt = "INSERT INTO receipt (Customer_ID, Total, Date, Em_Username) "
                        + "VALUES (?, ?, GETDATE(), ?)";
                PreparedStatement prepare = connect.prepareStatement(insertReceipt);
                prepare.setInt(1, data.cID);
                prepare.setDouble(2, totalP);
                prepare.setString(3, data.username);
                prepare.executeUpdate();

                // Generate receipt (via lambda or controller method)
                if (generateReceipt != null) {
                    generateReceipt.run();
                }

                // Success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment Successful");
                DialogUtils.applyBakeryIcon(alert); 
                alert.setHeaderText(null);
                showAlert("Payment completed successfully!\n"
                        + "Total: $" + String.format("%.2f", totalP) + "\n"
                        + "Amount Received: $" + String.format("%.2f", amountReceived) + "\n"
                        + "Change: $" + String.format("%.2f", change),
                        Alert.AlertType.INFORMATION);

                // Reset fields
                amountField.setText("");
                if (receiptButton != null) {
                    receiptButton.setDisable(false);
                }

                if (Main_FormController.instance != null) {
                    Main_FormController.instance.customersShowData();
                }

            } else {
                showAlert("Payment cancelled.", Alert.AlertType.WARNING);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Payment processing failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        dashboardSubject.notifyObservers();
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Payment");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogUtils.applyBakeryIcon(alert); 
        alert.showAndWait();
    }

}