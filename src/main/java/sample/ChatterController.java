package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import server.PrimitiveDateBase;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class ChatterController extends MyController {

    @FXML
    private ComboBox<String> friendBox;
    @FXML
    private TextArea messageToSend;

    @FXML
    private ListView<?> textMessages;


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
    public void initialize() throws URISyntaxException, FileNotFoundException {
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
    void logOut(ActionEvent event) throws IOException {
        changeScene("loginScene.fxml", event);
    }

    @FXML
    void sendMessage(ActionEvent event) {
        System.out.println("Message send attemp: "+ messageToSend.getText()+"\nto:"+friendBox.getValue().toString());
    }


}
