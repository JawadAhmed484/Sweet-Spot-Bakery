package thesweetspot;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import thesweetspot.patterns.chainofresponsibility.AnswerValidator;
import thesweetspot.patterns.chainofresponsibility.ConfirmPasswordValidator;
import thesweetspot.patterns.chainofresponsibility.PasswordValidator;
import thesweetspot.patterns.chainofresponsibility.QuestionValidator;
import thesweetspot.patterns.chainofresponsibility.UsernameValidator;
import thesweetspot.patterns.chainofresponsibility.validationHandler;
import thesweetspot.patterns.mediator.formMediator;
import thesweetspot.patterns.singleton.DBConnection;

public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField fp_answer;

    @FXML
    private Button fp_back;

    @FXML
    private Button fp_proceedBtn;

    @FXML
    private ComboBox<?> fp_question;

    @FXML
    private AnchorPane fp_questionForm;

    @FXML
    private TextField fp_username;

    @FXML
    private Button np_back;

    @FXML
    private Button np_changePassBtn;

    @FXML
    private TextField np_confirmPassword;

    @FXML
    private AnchorPane np_newPassForm;

    @FXML
    private TextField np_newPassword;

    @FXML
    private Hyperlink si_forgotPass;

    @FXML
    private Button si_loginBtn;

    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_username;

    @FXML
    private Button side_CreateBtn;

    @FXML
    private Button side_alreadyHave;

    @FXML
    private AnchorPane side_form;

    @FXML
    private TextField su_answer;

    @FXML
    private PasswordField su_password;

    @FXML
    private ComboBox<?> su_question;

    @FXML
    private Button su_signupBtn;

    @FXML
    private AnchorPane su_signupForm;

    @FXML
    private TextField su_username;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private Alert alert;

    private formMediator formMediator = new formMediator();

    public void loginBtn() {

        validationHandler usernameValidator = new UsernameValidator(si_username);
        validationHandler passwordValidator = new PasswordValidator(si_password);

        usernameValidator.setNext(passwordValidator);

        if (!usernameValidator.handle()) {
            return; // stop if validation fails
        }
        command login = new LoginCommand(si_username, si_password, si_loginBtn);
        login.execute();

    }

    public void regBtn() {

        validationHandler usernameValidator = new UsernameValidator(su_username);
        validationHandler passwordValidator = new PasswordValidator(su_password);
        validationHandler questionValidator = new QuestionValidator(su_question);
        validationHandler answerValidator = new AnswerValidator(su_answer);

        // Chain validators
        usernameValidator.setNext(passwordValidator)
                .setNext(questionValidator)
                .setNext(answerValidator);

        // Run validation chain
        if (!usernameValidator.handle()) {
            return; // stop if validation fails
        }
        command register = new RegisterCommand(su_username, su_password, su_question, su_answer);
        register.execute();

        TranslateTransition slider = new TranslateTransition();

        slider.setNode(side_form);
        slider.setToX(0);
        slider.setDuration(Duration.seconds(.5));

        slider.setOnFinished((ActionEvent e) -> {
            side_alreadyHave.setVisible(false);
            side_CreateBtn.setVisible(true);
        });

        slider.play();

    }

    private String[] questionList = {"What is your favorite Color?", "What is your favorite food?", "what is your birth date?"};

    public void regLquestionList() {
        List<String> listQ = new ArrayList<>();

        for (String data : questionList) {
            listQ.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listQ);
        su_question.setItems(listData);
    }

    public void switchForgotPass() {
        formMediator.switchTo("forgot");

        forgotPassQuestionList();
    }

    public void proceedBtn() {

        validationHandler usernameValidator = new UsernameValidator(fp_username);
        validationHandler questionValidator = new QuestionValidator(fp_question);
        validationHandler answerValidator = new AnswerValidator(fp_answer);

        usernameValidator.setNext(questionValidator).setNext(answerValidator);

        if (!usernameValidator.handle()) {
            return; // stop if validation fails
        } else {

            String selectData = "SELECT UserName, Question, Answer FROM users WHERE UserName = ? AND Question = ? AND Answer = ?";
            connect = DBConnection.getInstance();

            try {

                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, fp_username.getText());
                prepare.setString(2, (String) fp_question.getSelectionModel().getSelectedItem());
                prepare.setString(3, fp_answer.getText());

                result = prepare.executeQuery();

                if (result.next()) {
                    formMediator.transferForgotDataToNewPass();
                    formMediator.switchTo("newpass");
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Information");
                    DialogUtils.applyBakeryIcon(alert);
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void changePassBtn() {

        validationHandler newPasswordValidator = new PasswordValidator(np_newPassword);
        validationHandler confirmPasswordValidator = new ConfirmPasswordValidator(np_confirmPassword, np_newPassword);

        newPasswordValidator.setNext(confirmPasswordValidator);

        if (!newPasswordValidator.handle()) {
            return; // stop if validation fails
        } else {

            if (np_newPassword.getText().equals(np_confirmPassword.getText())) {
                String getDate = "SELECT Date FROM users WHERE UserName = '"
                        + fp_username.getText() + "'";

                connect = DBConnection.getInstance();

                try {

                    prepare = connect.prepareStatement(getDate);
                    result = prepare.executeQuery();

                    String date = "";
                    if (result.next()) {
                        date = result.getString("date");
                    }

                    String updatePass = "UPDATE users SET Password = '"
                            + np_newPassword.getText() + "', Question = '"
                            + fp_question.getSelectionModel().getSelectedItem() + "', Answer = '"
                            + fp_answer.getText() + "', Date = '"
                            + date + "' WHERE UserName = '"
                            + fp_username.getText() + "'";

                    prepare = connect.prepareStatement(updatePass);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully changed Password!");
                    DialogUtils.applyBakeryIcon(alert);
                    alert.showAndWait();

                    formMediator.resetFields(); 
                    formMediator.switchTo("login");

                    // TO CLEAR FIELDS
                    np_confirmPassword.setText("");
                    np_newPassword.setText("");
                    fp_question.getSelectionModel().clearSelection();
                    fp_answer.setText("");
                    fp_username.setText("");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");

                alert.setHeaderText(null);
                alert.setContentText("Not match");
                DialogUtils.applyBakeryIcon(alert);
                alert.showAndWait();
            }
        }
    }

    public void forgotPassQuestionList() {

        List<String> listQ = new ArrayList<>();

        for (String data : questionList) {
            listQ.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listQ);
        fp_question.setItems(listData);

    }

    public void backToLoginForm() {
        formMediator.switchTo("login");
    }

    public void backToQuestionForm() {
        formMediator.switchTo("forgot");
    }

    public void switchForm(ActionEvent event) {

        TranslateTransition slider = new TranslateTransition();

        if (event.getSource() == side_CreateBtn) {
            slider.setNode(side_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(true);
                side_CreateBtn.setVisible(false);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);

                regLquestionList();
            });

            slider.play();
        } else if (event.getSource() == side_alreadyHave) {
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(.5));

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(false);
                side_CreateBtn.setVisible(true);

                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
            });

            slider.play();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        formMediator.setForms(si_loginForm, su_signupForm, fp_questionForm, np_newPassForm);
        formMediator.setSharedFields(fp_username, fp_question, fp_answer);
    }

}
