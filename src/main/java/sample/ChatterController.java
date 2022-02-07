package sample;

import client.Conversation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonToken;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import javafx.util.Pair;
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
import java.util.concurrent.CompletableFuture;

import static sample.weather.WeatherDownloader.getDescriptionAndCurrentTemp;

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

    @FXML
    private TextField newFriendField;

    @FXML
    private Button addFriendButton;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label descriptionLabel;


    private synchronized static File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = ChatterController.class.getClassLoader();
        URL resource = classLoader.getResource("sample/"+fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }

    }

    @FXML
    void handleAddFriend(ActionEvent event){
        String newFriendEmail = newFriendField.getText();
        newFriendField.clear();
        if(newFriendEmail != null) {
            boolean isThereAlready = friendBox.getItems().stream().anyMatch(newFriendEmail::equals);
            if(!isThereAlready) {
                friendBox.getItems().add(newFriendEmail);
            }
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
        initRefresher();
        Platform.runLater(new Thread(this::initWeather));


    }

    private void initWeather(){
        Pair<String, String> weather= null;
        try {
            weather = getDescriptionAndCurrentTemp();
            descriptionLabel.setText(descriptionLabel.getText()+" "+ weather.getKey());
            temperatureLabel.setText(temperatureLabel.getText()+" "+ weather.getValue());

        } catch (InterruptedException | IOException | URISyntaxException e) {
            System.out.println("Weather initialize failed.");
            e.printStackTrace();
        }

    }
    void initRefresher(){
        Timeline refresher = new Timeline(
                new KeyFrame(Duration.seconds(2),
                        event -> downloadMessagesForCurrentSelectedReceiver()));
        refresher.setCycleCount(Timeline.INDEFINITE);
        refresher.play();
    }
    @FXML
    void logOut(ActionEvent event) throws IOException{
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
            messageToSend.clear();
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
        if(currentSelectedReceiver == null){
            return;
        }
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
