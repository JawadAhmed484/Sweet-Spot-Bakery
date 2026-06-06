
package thesweetspot.patterns.chainofresponsibility;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import thesweetspot.DialogUtils;

public class PasswordValidator extends validationHandler {
    private final TextField passwordField;

    public PasswordValidator(TextField passwordField) {
        this.passwordField = passwordField;
    }

    @Override
    protected boolean validate() {
        String password = passwordField.getText();
        
        if (password.isEmpty()) {
            showError("Password field is empty.");
            return false;
        }
        if (password.length() < 5) {
            showError("Password must be at least 5 characters long.");
            return false;
        }return true;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        DialogUtils.applyBakeryIcon(alert);
        alert.showAndWait();
    }
}
