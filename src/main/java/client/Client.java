package client;

import javafx.stage.Stage;
import utils.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Client {
    Socket socket;
    String host;
    boolean exit = false;
    int port;
    public final Sender sender = new Sender();
    public final Receiver receiver = new Receiver();

    public Client(String host, int port) throws InterruptedException, IOException {
        this.host = host;
        this.port = port;

    }
    public void connect() throws IOException, ConnectException{
        socket = new Socket(host, port);
        socket.setKeepAlive(true);
        //int timeoutInterval = 200;
        //socket.setSoTimeout(timeoutInterval);
    }
    public void startListener() throws IOException {
        this.receiver.start(socket);
        this.sender.start(socket);
    }

    public void addRequest(Request request) throws InterruptedException {
        if (request != null){
            sender.addRequest(request);
            if(this.sender.getState() == Thread.State.WAITING){
                synchronized (this.sender){
                    this.sender.notify();
                }
            }
        }

    }

    public static class Sender extends Thread{
        ObjectOutputStream objectOutputStream;
        ArrayBlockingQueue<Request> requests;
        boolean senderExit = false;

        Sender() throws IOException {
            super("Sender thread.");
            this.requests = new ArrayBlockingQueue<>(100);
        }
        public synchronized void start(Socket socket) throws IOException {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            super.start();
        }

        @Override
        public void run(){
            while (!senderExit){
                try{
                    sendAll();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        public void addRequest(Request request){
            this.requests.add(request);
        }
        private void sendAll() throws IOException, InterruptedException {
            if(requests.size() ==0){
                synchronized (this){
                    this.wait();
                }
            }
            Set<Request> payload = new HashSet<>(requests);
            System.out.println("Sending requests.");
            objectOutputStream.writeObject(payload);
            System.out.println("Send requests..");
            reinitializeRequestQueue();
        }
        private synchronized void reinitializeRequestQueue(){
            requests = null;
            requests = new ArrayBlockingQueue<>(1000);
        }
    }
    public class Receiver extends Thread{
        ObjectInputStream objectInputStream;
        ArrayBlockingQueue<Response> responses;
        boolean exit = false;
        Receiver(){
            super("Receiver thread.");
            this.responses = new ArrayBlockingQueue<>(100);
        }

        public synchronized void start(Socket socket) throws IOException {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            super.start();
        }
        @Override
        public void run() {
            while (true){
                try{
                    receiveAll();
                }catch (SocketTimeoutException ignored){
                } catch (IOException e) {
                    System.out.println("IO excetion");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted exception");
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found exception");
                    e.printStackTrace();
                }
            }
        }
        public void exit(){
            exit = true;
        }

        private void addResponse(Response response) throws InterruptedException {
            if(response != null){
                this.responses.put(response);
            }
        }

        public Response getResponse() throws InterruptedException {
            if (responses.size() > 0){
                return responses.take();
            }
            return null;
        }

        private void reinitializeRequestQueue(){
            responses = null;
            responses = new ArrayBlockingQueue<>(100);
        }
        private void receiveAll() throws IOException, ClassNotFoundException, InterruptedException {
            Set<Response> responses = (Set<Response>) objectInputStream.readObject();
            System.out.println("Read responses.");

            for(Response response: responses){
                this.addResponse(response);
            }

        }
    }
}