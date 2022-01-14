package server;

import jdk.jfr.Frequency;
import utils.DataModel;
import utils.Request;
import utils.Response;

import javax.print.DocFlavor;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

// ClientHandler class
class ClientHandler implements Runnable
{
    boolean exit = false;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    final Socket s;
    Request request;
    Response response;

    // Constructor
    public ClientHandler(Socket s) {
        this.s = s;

    }
    private Request readRequest() throws IOException, ClassNotFoundException{
        Request request = (Request) this.objectInputStream.readObject();
        System.out.println(request);
        return request;
    }

    private synchronized void sendData(Response response) throws IOException {
        System.out.println("Sending response.");
        System.out.println(response);
        this.objectOutputStream.writeObject(response);
        this.objectOutputStream.flush();
    }

    private Response handleRequest(Request request) {
        if (request.requestType == DataModel.RequestType.REGISTER) {

        } else if (request.requestType == DataModel.RequestType.LOGIN) {

        } else if (request.requestType == DataModel.RequestType.LOGOUT) {

        } else if (request.requestType == DataModel.RequestType.CHAT_MESSAGE) {

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
                    this.request = this.readRequest();
                    this.response = this.handleRequest(request);
                    this.sendData(this.response);

                } catch (IOException | ClassNotFoundException e) {
                    //e.printStackTrace();
                    this.exit = true;
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
