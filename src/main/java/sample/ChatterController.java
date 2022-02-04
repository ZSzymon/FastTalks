package sample;

import client.Conversation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import utils.DataModel;
import utils.Message;
import utils.Request;
import utils.Response;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Array;
import java.util.*;

public class ChatterController extends MyController {

    @FXML
    private ComboBox<String> friendBox;
    @FXML
    private TextArea messageToSend;

    @FXML
    private ListView<String> textMessages;

    @FXML
    private Label errorLabel;

    @FXML
    private Label loggedAsLabel;

    private synchronized static File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = ChatterController.class.getClassLoader();
        URL resource = classLoader.getResource("sample/"+fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }

    }

    public void initComboBoxList() throws URISyntaxException, FileNotFoundException {
        Gson gson = new Gson();
        File file = getFileFromResource("friends.json");
        BufferedReader br = new BufferedReader(new FileReader(file));
        Type type = new TypeToken<Map<String,List<String>>>(){}.getType();
        Map<String, List<String>> friends = gson.fromJson(br, type);
        ObservableList<String> options =
                FXCollections.observableArrayList(friends.get("friends"));
        friendBox.setItems(options);
    }
    @FXML
    public void initialize() throws URISyntaxException, FileNotFoundException {
        initComboBoxList();
        loggedAsLabel.setText("Logged as: "+ Main.userInfo.getKey());
    }

    @FXML
    void logOut(ActionEvent event) throws IOException {
        changeScene("loginScene.fxml", event);
    }

    @FXML
    void sendMessage(ActionEvent event) {
        String messageText = messageToSend.getText();
        String sender = Main.userInfo.getKey();
        String receiver = friendBox.getValue();
        if(receiver == null){
            errorLabel.setText("Choose receiver.");
            return;
        }

        if(receiver.equals("") || messageText.equals("")){
            return;
        }
        boolean result = Main.clientBackend.sendChatMessage(sender, receiver, messageText);

        if(result){
            Conversation conversation = Main.clientBackend.getConversation(receiver);
            updateMessagesList(conversation);
        }else{
            errorLabel.setText("Error has occered on server.");
        }

    }
    @FXML
    void downloadMessagesForCurrentSelectedReceiver(){
        String email = Main.userInfo.getKey();
        if(email == null){
            return;
        }
        Main.clientBackend.downloadConversations(email);
        String currentSelectedReceiver = this.friendBox.getValue();
        Conversation conversation = Main.clientBackend.getConversation(currentSelectedReceiver);
        updateMessagesList(conversation);
    }
    void updateMessagesList(Conversation conversation){
        if(conversation == null){
            this.textMessages.getItems().clear();
            return;
        }
        List<String> strings = conversation.toStringList();
        ObservableList<String> messages =
                FXCollections.observableArrayList(strings);

        this.textMessages.setItems(messages);
    }


}
