package server;

import utils.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashSet;
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
        this.isActive = true;
        this.messages= new HashSet<>();
    }
    public void updateStreams(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream){
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }
    private void removeStreams() throws IOException {
        this.objectOutputStream.close();
        this.objectInputStream.close();
        this.objectInputStream = null;
        this.objectOutputStream = null;
    }
    public void login(){
        isActive = true;
        lastActive = LocalDateTime.now();
    }
    public void logout() {
        isActive = false;
        lastActive = LocalDateTime.now();
    }
}
