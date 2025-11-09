package thesweetspot;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import thesweetspot.Data.data;
import thesweetspot.Data.productData;
import thesweetspot.patterns.observer.dashboardSubject;
import thesweetspot.patterns.singleton.DBConnection;

public interface command {

    void execute();
}

class LoginCommand implements command {

    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button loginButton;

    public LoginCommand(TextField usernameField, PasswordField passwordField, Button loginButton) {
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.loginButton = loginButton;
    }

    @Override
    public void execute() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Alert alert;
        if (username.isEmpty() || password.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter username and password.");
            DialogUtils.applyBakeryIcon(alert); 
            alert.showAndWait();
            return;
        }

        try {
            Connection connect = DBConnection.getInstance();
            String sql = "SELECT UserName, Password FROM users WHERE UserName = ? and Password = ?";
            PreparedStatement prepare = connect.prepareStatement(sql);
            prepare.setString(1, username);
            prepare.setString(2, password);
            ResultSet result = prepare.executeQuery();

            if (result.next()) {
                data.username = username;

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Success");
                alert.setHeaderText(null);
                alert.setContentText("Successfully logged in!");
                DialogUtils.applyBakeryIcon(alert); 
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("main_Form.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("The Sweet Spot Bakery");
                stage.setScene(scene);

                Image image = new Image("resources/Bakery(Main Form).jpg");
                stage.getIcons().add(image);

                stage.show();
                loginButton.getScene().getWindow().hide();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect credentials.");
                DialogUtils.applyBakeryIcon(alert); 
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class RegisterCommand implements command {

    private final TextField usernameField;
    private final PasswordField passwordField;
    private final ComboBox<?> questionBox;
    private final TextField answerField;

    public RegisterCommand(TextField usernameField, PasswordField passwordField,
            ComboBox<?> questionBox, TextField answerField) {
        this.usernameField = usernameField;
        this.passwordField = passwordField;
        this.questionBox = questionBox;
        this.answerField = answerField;
    }

    @Override
    public void execute() {
        try {
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()
                    || questionBox.getSelectionModel().getSelectedItem() == null
                    || answerField.getText().isEmpty()) {
                showAlert("Please fill all blank fields", Alert.AlertType.ERROR);
                return;
            }

            Connection connect = DBConnection.getInstance();
            String checkUsername = "SELECT UserName FROM users WHERE UserName = ?";
            PreparedStatement prepare = connect.prepareStatement(checkUsername);
            prepare.setString(1, usernameField.getText());
            ResultSet result = prepare.executeQuery();

            if (result.next()) {
                showAlert("Username already taken", Alert.AlertType.ERROR);
                return;
            }

            String insert = "INSERT INTO users (UserName, Password, Question, Answer, Date) VALUES (?, ?, ?, ?, ?)";
            prepare = connect.prepareStatement(insert);
            prepare.setString(1, usernameField.getText());
            prepare.setString(2, passwordField.getText());
            prepare.setString(3, (String) questionBox.getSelectionModel().getSelectedItem());
            prepare.setString(4, answerField.getText());
            prepare.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
            prepare.executeUpdate();

            showAlert("Account registered successfully!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Register");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogUtils.applyBakeryIcon(alert); 
        alert.showAndWait();
    }
}

class AddProductCommand implements command {

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

class UpdateProductCommand implements command {

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

class DeleteProductCommand implements command {

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
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to delete");
            DialogUtils.applyBakeryIcon(alert); 
            alert.showAndWait();
            return;
        }

        alert = new Alert(AlertType.CONFIRMATION);
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

                alert = new Alert(AlertType.INFORMATION);
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
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Cancelled");
            DialogUtils.applyBakeryIcon(alert); 
            alert.showAndWait();
        }
    }
}

class ClearFormCommand implements command {

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

class ImportImageCommand implements command {

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

class PayCommand implements command {

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

class RemoveFromCartCommand implements command {

    private final TableView<productData> tableView;
    private final Runnable refreshOrderData;
    private final Runnable refreshTotal;
    private final Runnable refreshCards;

    public RemoveFromCartCommand(
            TableView<productData> tableView,
            Runnable refreshOrderData,
            Runnable refreshTotal,
            Runnable refreshCards
    ) {
        this.tableView = tableView;
        this.refreshOrderData = refreshOrderData;
        this.refreshTotal = refreshTotal;
        this.refreshCards = refreshCards;
    }

    @Override
    public void execute() {
        productData selectedItem = tableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showAlert("Please select an item to remove", Alert.AlertType.ERROR);
            return;
        }

        if (data.cID == null) {
            showAlert("No items in cart", Alert.AlertType.ERROR);
            return;
        }

        try {
            Connection connect = DBConnection.getInstance();
            String sql = "EXEC sp_RemoveFromCart ?, ?";
            PreparedStatement prepare = connect.prepareStatement(sql);
            prepare.setInt(1, data.cID);
            prepare.setString(2, selectedItem.getProductId());
            prepare.executeUpdate();

            showAlert("Item removed from cart", Alert.AlertType.INFORMATION);

            // GUI Refresh
            if (refreshOrderData != null) {
                refreshOrderData.run();
            }
            if (refreshTotal != null) {
                refreshTotal.run();
            }
            if (refreshCards != null) {
                refreshCards.run();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to remove item: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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

class ClearCartCommand implements command {

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
