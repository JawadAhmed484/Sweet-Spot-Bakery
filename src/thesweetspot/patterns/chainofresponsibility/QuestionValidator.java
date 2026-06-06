
package thesweetspot.patterns.chainofresponsibility;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import thesweetspot.DialogUtils;

public class QuestionValidator extends validationHandler {
    private final ComboBox<?> questionBox;

    public QuestionValidator(ComboBox<?> questionBox) {
        this.questionBox = questionBox;
    }

    @Override
    protected boolean validate() {
        if (questionBox.getSelectionModel().getSelectedItem() == null) {
            showError("Please select a security question.");
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
