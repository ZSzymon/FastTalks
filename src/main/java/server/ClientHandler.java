package server;

import utils.*;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;

// ClientHandler class
public class ClientHandler extends Thread
{
    boolean exit = false;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    final Socket s;
    public static int i =0;
    // Constructor
    public ClientHandler(Socket s) {
        super("Client Handler:" + (i+1));
        this.s = s;
    }

    private void sendData(Set<Response> responses) throws IOException {
        System.out.println("Sending response.");
        System.out.println(responses);
        this.objectOutputStream.writeObject(responses);
        this.objectOutputStream.flush();
    }

    private Set<Response> handleRequests(Set<Request> requests){
        Set<Response> responses = new HashSet<>();
        for(Request request: requests){
            responses.add(this.handleRequest(request));
        }
        return responses;
    }
    private Response handleRequest(Request request) {

        if (request.requestType == DataModel.RequestType.REGISTER) {

        } else if (request.requestType == DataModel.RequestType.LOGIN) {

        } else if (request.requestType == DataModel.RequestType.LOGOUT) {

        } else if (request.requestType == DataModel.RequestType.CHAT_MESSAGE) {

        } else if (request.requestType == DataModel.RequestType.HEARTBEAT){

        }
        return new Response(null, request.requestId, DataModel.ResponseCode.OK);
    }


    public void closeConnection(){
        this.exit = true;
    }
    @Override
    public void run()
    {
        exit = false;
        try {
            this.objectOutputStream = new ObjectOutputStream(s.getOutputStream());
            this.objectInputStream = new ObjectInputStream(s.getInputStream());

            while (!exit) {
                try {
                    Set<Request> requests = (HashSet<Request>) objectInputStream.readObject();
                    Set<Response> responses = this.handleRequests(requests);
                    this.sendData(responses);
                }catch (SocketTimeoutException ste){
                    //"ignore"
                } catch (ClassNotFoundException e) {
                    this.exit = true;
                    closeStreams();
                } catch (SocketException ignored){
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeStreams() throws IOException {
        this.objectInputStream.close();
        this.objectOutputStream.close();
        this.s.close();
    }
}
