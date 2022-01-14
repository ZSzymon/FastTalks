package client;

import javafx.stage.Stage;
import utils.Request;
import utils.Response;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

public class Client {
    Socket socket;
    String host;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    Queue<Request> requests;
    Queue<Response> responses;
    boolean exit = false;
    int port;

    public Client(String host, int port) throws InterruptedException {
        this.host = host;
        this.port = port;
        this.requests = new LinkedList<>();
        this.responses = new LinkedList<>();
    }

    public void startListener(){
        this.messageListener.start()
    }


}