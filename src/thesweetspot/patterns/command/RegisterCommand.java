
package thesweetspot.patterns.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import thesweetspot.DialogUtils;
import thesweetspot.patterns.singleton.DBConnection;

public class RegisterCommand implements command {

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
