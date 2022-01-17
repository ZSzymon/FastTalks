package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController extends MyController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField login;

    @FXML
    void login(ActionEvent event) throws IOException {
//        System.out.println("Login attempt:"+loginField.getText()+"\n password: "+passwordField.getText());
        changeScene("chatter.fxml", event);
    }

    @FXML
    void openRegisterScene(ActionEvent event) throws IOException {
        changeScene("registerScene.fxml", event);
    }



}
