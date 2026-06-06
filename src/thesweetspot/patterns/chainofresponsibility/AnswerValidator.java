
package thesweetspot.patterns.chainofresponsibility;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import thesweetspot.DialogUtils;

public class AnswerValidator extends validationHandler {
    private final TextField answerField;

    public AnswerValidator(TextField answerField) {
        this.answerField = answerField;
    }

    @Override
    protected boolean validate() {
        if (answerField.getText().isEmpty()) {
            showError("Answer field is empty.");
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
