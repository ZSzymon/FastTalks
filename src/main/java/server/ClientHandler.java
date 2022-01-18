package server;

import utils.*;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

// ClientHandler class
public class ClientHandler extends Thread
{
    boolean exit = false;
    public ObjectInputStream objectInputStream;
    public ObjectOutputStream objectOutputStream;
    final Socket s;
    public Set<Request> requests;

    // Constructor
    public ClientHandler(Socket s) {
        this.s = s;
    }

    private synchronized void sendData(Set<Response> responses) throws IOException {
        this.objectOutputStream.writeObject(responses);
        this.objectOutputStream.flush();
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
                    requests = (HashSet<Request>) objectInputStream.readObject();
                    Server.requestQueue.add(this);
                    if( Server.requestAnalyser.getState() == Thread.State.WAITING )
                    {
                        synchronized (Server.requestAnalyser) {
                            Server.requestAnalyser.notify();
                        }
                    }
                    //requestAnalyser will send responses back to client.
                }catch (SocketTimeoutException ste){
                    //"ignore"
                } catch (ClassNotFoundException e) {
                    this.exit = true;
                    closeStreams();
                } catch (SocketException se){
                    System.out.println("Socket exception. Connection reset.");
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
