package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DataModel;
import utils.Request;
import utils.Response;


import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class RegisterController extends MyController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField1;

    @FXML
    private PasswordField passwordField2;

    @FXML
    private Label errorLabel;

    @FXML
    void OpenLoginScene(ActionEvent event) throws IOException {
        changeScene("loginScene.fxml", event);
    }

    @FXML
    void register(ActionEvent event) throws IOException {
        System.out.println("Register attempt: " + this.emailField.getText() + "\nPassword:" +
                this.passwordField1.getText()+"\nPassword2: "+this.passwordField2.getText());
        if(Arrays.asList(this.passwordField1.getText(), this.passwordField2.getText(), this.emailField.getText()).contains("")){
            errorLabel.setText("Login or password can't be empty.");
        }
        Request request = Request.registerRequest(this.emailField.getText(),
                this.passwordField1.getText(),
                this.passwordField2.getText());
        Main.clientBackend.addRequest(request);
        Response response = Main.clientBackend.receiver.waitForResponse(request.requestId);
//
        if(response.responseCode.equals(DataModel.ResponseCode.OK)){
            this.errorLabel.setText("");
            changeScene("loginScene.fxml", event);

        }else{
            this.errorLabel.setText("Wrong login or password.");
        }

    }

}
