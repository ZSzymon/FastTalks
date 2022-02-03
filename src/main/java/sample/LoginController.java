package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.DataModel;
import utils.Request;
import utils.Response;

import java.io.IOException;

public class LoginController extends MyController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField login;

    @FXML
    private Label errorLabel;

    @FXML
    void login(ActionEvent event) throws IOException {
        System.out.println("Login attempt:"+ loginField.getText()+"\n password: "+passwordField.getText());
        String login = this.loginField.getText();
        String password = this.passwordField.getText();
        if(login.equals("") || password.equals("")){
            errorLabel.setText("Login or password can't be empty.");
            return;
        }

        Request request = Request.loginRequest(this.loginField.getText(), this.passwordField.getText());


        Main.clientBackend.addRequest(request);
        Response response = Main.clientBackend.receiver.waitForResponse(request.requestId);
//
        if(response.responseCode.equals(DataModel.ResponseCode.OK)){
            errorLabel.setText("");
            changeScene("chatter.fxml", event);
        }else{
            errorLabel.setText("Wrong login or password.");
        }




    }

    @FXML
    void openRegisterScene(ActionEvent event) throws IOException {



        changeScene("registerScene.fxml", event);
    }



}
