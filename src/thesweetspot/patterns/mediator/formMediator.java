package thesweetspot.patterns.mediator;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import thesweetspot.*;
import javafx.scene.layout.AnchorPane;


    public class formMediator {

    private AnchorPane loginForm, signupForm, forgotForm, newPassForm;

    private TextField sharedUsername;
    private ComboBox<?> sharedQuestion;
    private TextField sharedAnswer;

    public void setForms(AnchorPane login, AnchorPane signup, AnchorPane forgot, AnchorPane newPass) {
        this.loginForm = login;
        this.signupForm = signup;
        this.forgotForm = forgot;
        this.newPassForm = newPass;
    }

    public void setSharedFields(TextField username, ComboBox<?> question, TextField answer) {
        this.sharedUsername = username;
        this.sharedQuestion = question;
        this.sharedAnswer = answer;
    }

    public void switchTo(String formName) {
        loginForm.setVisible(false);
        signupForm.setVisible(false);
        forgotForm.setVisible(false);
        newPassForm.setVisible(false);

        switch (formName.toLowerCase()) {
            case "login":
                loginForm.setVisible(true);
                break;
            case "signup":
                signupForm.setVisible(true);
                break;
            case "forgot":
                forgotForm.setVisible(true);
                break;
            case "newpass":
                newPassForm.setVisible(true);
                break;
        }
    }

    public void transferForgotDataToNewPass() {
        if (sharedUsername != null && sharedUsername.getText() != null) {
            System.out.println("[Mediator] Username transferred: " + sharedUsername.getText());
        }
    }

    public void resetFields() {
        if (sharedUsername != null) sharedUsername.clear();
        if (sharedAnswer != null) sharedAnswer.clear();
        if (sharedQuestion != null) sharedQuestion.getSelectionModel().clearSelection();
    }
}