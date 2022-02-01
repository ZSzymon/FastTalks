package server;

import utils.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Set;

public class User {
    String email;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    Set<Message> messages;
    LocalDateTime lastActive;
    boolean isActive;
    public User(String email, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.email = email;
        updateStreams(objectInputStream, objectOutputStream);
        isActive = true;
    }
    public void updateStreams(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream){
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }
    private void removeStreams(){
        this.objectInputStream = null;
        this.objectOutputStream = null;
    }
    public void login(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream){
        updateStreams(objectInputStream, objectOutputStream);
        isActive = true;
        lastActive = LocalDateTime.now();
    }
    public void logout(){
        removeStreams();
        isActive = false;
        lastActive = LocalDateTime.now();
    }
}
