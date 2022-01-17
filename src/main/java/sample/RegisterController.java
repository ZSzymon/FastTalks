package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController extends MyController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField1;

    @FXML
    private PasswordField passwordField2;

    @FXML
    void OpenLoginScene(ActionEvent event) throws IOException {
        changeScene("/loginScene.fxml", event);
    }

    @FXML
    void register(ActionEvent event) {
        System.out.println("Register attempt: " + this.emailField.getText() + "\nPassword:" +
                this.passwordField1.getText()+"\nPassword2: "+this.passwordField2.getText());
    }

}
