
package thesweetspot.patterns.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import thesweetspot.DialogUtils;
import thesweetspot.Data.data;
import thesweetspot.patterns.singleton.DBConnection;

public class LoginCommand implements command {

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
