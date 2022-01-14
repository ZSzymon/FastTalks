package client;

import javafx.stage.Stage;
import sample.Main;
import utils.Request;
import utils.Response;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeoutException;

public class Client {
    Socket socket;
    String host;
    boolean exit = false;
    int port;
    public MessageListener messageListener;

    public Client(String host, int port) throws InterruptedException, IOException {
        this.host = host;
        this.port = port;
    }
    public void connect() throws IOException, ConnectException{
        socket = new Socket(host, port);
        //socket.setKeepAlive(true);
        //int timeoutInterval = 200;
        //socket.setSoTimeout(timeoutInterval);
        this.messageListener = new MessageListener(socket);
    }
    public void startListener(){
        this.messageListener.start();
    }

    public void addRequest(Request request) throws InterruptedException {
        if (request != null){
            messageListener.addRequest(request);
        }
    }

    public class MessageListener extends Thread{
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        ArrayBlockingQueue<Request> requests;
        ArrayBlockingQueue<Response> responses;
        boolean exit = false;
        MessageListener(Socket socket) throws IOException {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.requests = new ArrayBlockingQueue<>(1000);
            this.responses = new ArrayBlockingQueue<>(1000);
        }

        @Override
        public void run() {
            while (true){
                try{
                    sendAll();
                }catch (SocketTimeoutException ignored){

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void stopListener(){
            exit = true;
        }
        private synchronized void addRequest(Request request) throws InterruptedException {
            this.requests.put(request);
        }

        private void addResponse(Response response) throws InterruptedException {
            if(response != null){
                responses.put(response);
            }
        }

        public Response getResponse() throws InterruptedException {
            return responses.take();
        }

        public synchronized Response getResponse(int responseId){
            for(Response response: responses){
                if (response.responseId == responseId){
                    return response;
                }
            }
            return null;
        }

        private synchronized void sendAll() throws IOException, ClassNotFoundException, InterruptedException {

            for(Request request: requests){
                objectOutputStream.writeObject(request);
                Response response = (Response) objectInputStream.readObject();
                addResponse(response);
            }
            requests = null;
            requests = new ArrayBlockingQueue<>(1000);

        }
    }
}