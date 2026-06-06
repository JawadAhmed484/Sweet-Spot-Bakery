
package thesweetspot.patterns.chainofresponsibility;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import thesweetspot.DialogUtils;

public class ConfirmPasswordValidator extends validationHandler {
    private final TextField confirmPasswordField;
    private final TextField passwordField;

    public ConfirmPasswordValidator(TextField confirmPasswordField, TextField passwordField) {
        this.confirmPasswordField = confirmPasswordField;
        this.passwordField = passwordField;
    }

    @Override
    protected boolean validate() {
        if (confirmPasswordField.getText().isEmpty()) {
            showError("Confirm Password cannot be empty.");
            return false;
        }
        if (!confirmPasswordField.getText().equals(passwordField.getText())) {
            showError("Passwords do not match.");
            return false;
        }
        return true;
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
