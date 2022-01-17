package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import javafx.stage.Stage;

import java.io.IOException;

public class ChatterController extends MyController {

    @FXML
    private ComboBox<?> friendBox;
    @FXML
    private TextArea messageToSend;

    @FXML
    private ListView<?> textMessages;

    @FXML
    void logOut(ActionEvent event) throws IOException {
        changeScene("/loginScene.fxml", event);
    }

    @FXML
    void sendMessage(ActionEvent event) {
        System.out.println("Message send attemp: "+ messageToSend.getText()+"\nto:"+friendBox.getValue().toString());
    }

}
