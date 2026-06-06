
package thesweetspot.patterns.chainofresponsibility;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import thesweetspot.DialogUtils;

public class UsernameValidator extends validationHandler {
    private final TextField usernameField;

    public UsernameValidator(TextField usernameField) {
        this.usernameField = usernameField;
    }

    @Override
    protected boolean validate() {
        if (usernameField.getText().isEmpty()) {
            showError("Username field is empty.");
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
