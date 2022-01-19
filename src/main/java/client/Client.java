package client;

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
    public MessageListener messageListener;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;

    }
    public void connect() throws IOException, ConnectException{
        socket = new Socket(host, port);
        socket.setKeepAlive(true);
        this.messageListener = new MessageListener(socket);
        this.messageListener.start();
    }


    public void addRequest(Request request) throws InterruptedException {
        if (request != null){
            messageListener.addRequest(request);
            if(!messageListener.isAlive()){
                messageListener.start();
            }
            if(messageListener.getState() == Thread.State.WAITING){
                synchronized (messageListener){
                    messageListener.notify();
                }
            }
        }
    }

    public class MessageListener extends Thread{
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        ArrayBlockingQueue<Request> requests;
        ArrayBlockingQueue<Response> responses;
        boolean exit = false;

        MessageListener(Socket socket) throws IOException {
            super();
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.requests = new ArrayBlockingQueue<>(1000);
            this.responses = new ArrayBlockingQueue<>(1000);
        }

        @Override
        public void run() {
            while (true){
                try{
                    sendAndReceiveAll();
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

        void stopListener(){
            exit = true;
        }
        private void addRequest(Request request) throws InterruptedException {
            this.requests.put(request);
            if(this.getState() == State.WAITING){
                synchronized (this){
                    this.notify();
                }
            }
        }

        private void addResponse(Response response) throws InterruptedException {
            if(response != null){
                responses.put(response);
            }
        }

        public Response getResponse() throws InterruptedException {
            return responses.take();
        }

        public synchronized Response getResponse(UUID responseId){
            for(Response response: responses){
                if (response.responseId == responseId){
                    return response;
                }
            }
            return null;
        }

        private synchronized void reinitializeRequestQueue(){
            requests = null;
            requests = new ArrayBlockingQueue<>(1000);
        }
        private synchronized void sendAndReceiveAll() throws IOException, ClassNotFoundException, InterruptedException {
            if(requests.size() == 0) {
                return;
            }
            Set<Request> payload = new HashSet<>(requests);
            for (Request request: payload){
                System.out.println("Send Request:"+ request.toString());
            }

            objectOutputStream.writeObject(payload);
            objectOutputStream.flush();
            Set<Response> responses = (HashSet<Response>) objectInputStream.readObject();
            for(Response response: responses){
                this.addResponse(response);
                System.out.println("Receive response: "+ response.toString());
            }

            reinitializeRequestQueue();
        }
    }
}