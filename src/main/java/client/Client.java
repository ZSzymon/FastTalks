package client;

import com.google.gson.reflect.TypeToken;
import utils.*;
import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Client {
    Socket socket;
    String host;
    boolean exit = false;
    int port;
    private final Sender sender = new Sender();
    public final Receiver receiver = new Receiver();
    private Map<String, Conversation> conversations = new HashMap<>();
    private ReentrantLock conversationLock = new ReentrantLock();
    public boolean isListenerStarted = false;
    public Client(String host, int port) throws InterruptedException, IOException {
        this.host = host;
        this.port = port;

    }
    public void connect() throws IOException{
        socket = new Socket(host, port);
        socket.setKeepAlive(true);
        //int timeoutInterval = 200;
        //socket.setSoTimeout(timeoutInterval);
    }
    public void stopListener(){
        if(sender != null){
            sender.exit();
        }
        if(receiver != null){
            receiver.exit();
        }
        isListenerStarted = false;
    }
    public void reconnectIn(int seconds) throws IOException{
        System.out.println("Trying to connect to server in: " + seconds + " seconds.");
        try {
            Thread.sleep(seconds*1000);
            connect();
            startListener();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void startListener() throws IOException {
        if(!isListenerStarted){
            this.receiver.start(socket);
            this.sender.start(socket);
            this.isListenerStarted = true;
        }

    }
    public boolean sendChatMessage(String senderEmail, String receiverEmail, String messageText){
        if(receiverEmail == null){
            return false;
        }

        if(receiverEmail.equals("") || messageText.equals("")){
            return false;
        }
        UUID requestId = UUID.randomUUID();
        Message message = new Message(senderEmail, receiverEmail, messageText);
        String messageString = message.toJson();

        HashMap<String, String> payload = new HashMap<>();
        payload.put("message", messageString);
        payload.put("email", senderEmail);

        Request request = new Request(senderEmail, payload, requestId, DataModel.RequestType.CHAT_MESSAGE);
        addRequest(request);
        Response response = this.receiver.waitForResponse(request.requestId);

        if (response.responseCode.equals(DataModel.ResponseCode.OK)){
            addToConversation(message);
        }
        return response.responseCode.equals(DataModel.ResponseCode.OK);

    }

    public void downloadConversations(String email){
        UUID requestId = UUID.randomUUID();
        Request request = new Request(email,null, requestId, DataModel.RequestType.DOWNLOAD_MESSAGES);
        addRequest(request);
        Response response = this.receiver.waitForResponse(request.requestId);
        Type type = new TypeToken<HashSet<Message>>(){}.getType();
        Set<Message> messages = (Set<Message>) Message.fromJson(response.content.get("payload"), type);
        messages.forEach(this::addToConversation);
    }

    public void clearMessages(){
        conversationLock.lock();
        try{
            conversations.clear();
        }finally {
            conversationLock.unlock();
        }
    }

    private String generateKey(String email1, String email2){
        String s = email1.length() > email2.length() ? email1 + email2 : email2 + email1;
        return s;
    }
    private void addToConversation(Message message){
        String key = generateKey(message.senderEmail, message.receiverEmail);
        if(conversations.get(key) == null){
            conversations.put(key, new Conversation(message));
        }else{
            conversations.get(key).add(message);
        }
    }
//    private void addToConversationAsSender(Message message) {
//        String receiver = message.receiverEmail;
//        if(conversations.get(receiver) == null){
//            conversations.put(receiver, new Conversation(message));
//        }else{
//            conversations.get(receiver).add(message);
//        }
//    }
//    private void addToConversationAsReceiver(Message message) {
//        String sender = message.senderEmail;
//        if(conversations.get(sender) == null){
//            conversations.put(sender, new Conversation(message));
//        }else{
//            conversations.get(sender).add(message);
//        }
//    }

    public Conversation getConversation(String firstEmail, String secondEmail){
        //Order of emails do not matter.
        String key = generateKey(firstEmail, secondEmail);
        return conversations.get(key);

    }

    public void addRequest(Request request){
        if (request==null)
            return;

        sender.addRequest(request);
        if(this.sender.getState() == Thread.State.WAITING){
            synchronized (this.sender){
                this.sender.notify();
            }
        }
    }

    public static class Sender extends Thread{
        ObjectOutputStream objectOutputStream;
        ArrayBlockingQueue<Request> requests;
        ReentrantLock requestLock = new ReentrantLock();
        boolean senderExit = false;

        Sender(){
            super("Sender thread.");
            this.requests = new ArrayBlockingQueue<>(100);
        }

        synchronized void start(Socket socket) throws IOException {
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
        public void exit(){
            senderExit = true;
        }
        public void addRequest(Request request){
            this.requestLock.lock();
            try{
                this.requests.add(request);
            }finally {
                requestLock.unlock();
            }
        }

        private void sendAll() throws IOException, InterruptedException {
            if(requests.size() ==0){
                synchronized (this){
                    this.wait();
                }
            }
            requestLock.lock();
            try{
                Set<Request> payload = new HashSet<>(requests);
                System.out.println("Sending: ");
                requests.forEach(System.out::println);
                objectOutputStream.writeObject(payload);
                reinitializeRequestQueue();
            }finally {
                requestLock.unlock();
            }
        }
        private synchronized void reinitializeRequestQueue(){
            requests = null;
            requests = new ArrayBlockingQueue<>(100);
        }
    }
    public class Receiver extends Thread{
        ObjectInputStream objectInputStream;
        HashMap<UUID, Response> responses;
        ReentrantLock responsesLock = new ReentrantLock();
        boolean exit = false;
        Receiver(){
            super("Receiver thread.");
            this.responses = new HashMap<>();
        }

        public synchronized void start(Socket socket) throws IOException {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            super.start();
        }
        @Override
        public void run() {
            while (!exit){
                try{
                    receiveAll();
                }catch (SocketTimeoutException ignored){
                } catch (IOException e) {
                    System.out.println("IO excetion");
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

        private void addResponse(Response response) {
            if(response != null){
                this.responses.put(response.responseId,response);
            }
        }

        public Response getAnyResponse()  {
            Response response=null;
            responsesLock.lock();
            try{
                if (responses.size() > 0){
                    response = responses.entrySet().iterator().next().getValue();
                }
            }finally {
                responsesLock.unlock();
            }
            return response;
        }

        public Response waitForResponse(UUID requestId){
            Response response = null;
            while (response == null){
                response = this.getResponse(requestId);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
        public Response getResponse(UUID uuid){
            Response response = null;
            try{
                responsesLock.lock();
                response = responses.get(uuid);
            }finally {
                responsesLock.unlock();
            }
            return response;
        }
        private void reinitializeRequestQueue(){
            responses = null;
            responses = new HashMap<>();
        }
        private void receiveAll() throws IOException, ClassNotFoundException {
            Set<Response> responses = (HashSet<Response>) objectInputStream.readObject();

            try{
                responsesLock.lock();
                System.out.println("Got Responses: ");
                for(Response response: responses){
                    System.out.println(response.toString());
                    this.addResponse(response);
                }
            }finally {
                responsesLock.unlock();
            }
        }
    }
}