package server;

import org.jetbrains.annotations.NotNull;
import utils.*;


import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

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
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendData(Set<Response> responses) throws IOException {
        System.out.println("Sending response.");
        System.out.println(responses);
        this.objectOutputStream.writeObject(responses);
        this.objectOutputStream.flush();
    }

    private Set<Response> handleRequests(Set<Request> requests) throws IOException, URISyntaxException {
        Set<Response> responses = new HashSet<>();
        for(Request request: requests){
            responses.add(this.handleRequest(request));

        }
        return responses;
    }
    private Response handleRequest(Request request) throws IOException, URISyntaxException {
        Response response = null;
        tryAddUser(request);
        if (request.requestType == DataModel.RequestType.REGISTER) {
            response = handleRegister(request);
        } else if (request.requestType == DataModel.RequestType.LOGIN) {
            response = handleLogin(request);
        } else if (request.requestType == DataModel.RequestType.LOGOUT) {
            response = handleLogout(request);
        } else if (request.requestType == DataModel.RequestType.CHAT_MESSAGE) {
            response = handleChatMessage(request);
        } else if(request.requestType == DataModel.RequestType.DOWNLOAD_MESSAGES){
            response = handleDownloadMessages(request);
        }else if (request.requestType == DataModel.RequestType.HEARTBEAT){
            response = new Response(null, request.requestId, DataModel.ResponseCode.OK);
        }
        return response;
    }

    private Response handleDownloadMessages(Request request){
        Response response = new Response(new HashMap<>(), request.requestId, null);
        String email = request.email;
        if(!exists(email)){
            response.responseCode = DataModel.ResponseCode.FAIL;
            response.content.put("details","Request need to contain email.");
            return response;
        }
        Set<Message> messages = getMessages(email);
        String messagesAsString = Message.collectionToJson(messages);
        response.content.put("payload", messagesAsString);
        response.responseCode = DataModel.ResponseCode.OK;
        return response;
    }

    private Set<Message> getMessages(String email){

        User user = getUser(email);
        if(!exists(user)){
            return null;
        }
        Set<Message> messages = user.messages;
        return messages;
    }
    private void tryAddUser(Request request){
        if(request.email != null){
            addToActiveUsers(request.email);
        }
    }
    public boolean exists(Object o){
        return o != null;
    }
    private Response handleChatMessage(Request request){
        Response response = new Response(new HashMap<>(), request.requestId, null);

        Message message = Message.fromJson(request.content.get("message"));
        User senderUser = getUser(message.senderEmail);
        User receiverUser = getUser(message.receiverEmail);
        boolean isAllOkey = true;
        if(exists(senderUser)){
            senderUser.messages.add(message);
        }else{
            isAllOkey = false;
            return response;
        }
        if(exists(receiverUser)){
            receiverUser.messages.add(message);
        }else{
            isAllOkey = false;
        }
        response.responseCode = isAllOkey ? DataModel.ResponseCode.OK : DataModel.ResponseCode.FAIL;
        return response;
    }


    private User getUser(String email){
        Server.usersLock.lock();
        User user;
        try{
            user = Server.users.get(email);
        }finally {
            Server.usersLock.unlock();
        }
        return user;
    }

    private User getUser(String email, String password){
        User user = null;
        boolean isInDb = false;
        Server.dbLock.lock();
        try {
            isInDb = Server.db.exist(email, password);

        }finally {
            Server.dbLock.unlock();
        }
        if(isInDb){
            user = getUser(email);
        }


        return user;
    }
    private Response handleRegister(Request request) throws IOException, URISyntaxException {
        //  public Response(Map<String, String>content, UUID responseId, ResponseCode responseCode)
        Response response = new Response(null, request.requestId, null);

        String email = request.content.get("email");
        String password1 = request.content.get("password1");
        String password2 = request.content.get("password2");
        Boolean isPasswordValid = password1.equals(password2);
        Boolean isSuccess = false;
        Server.dbLock.lock();
        try{
            Server.db.reload();
            isSuccess = Server.db.addUser(email, password1);

            //TODO: remove in develop mode.
            if (Config.DEBUG && email.equals("szymon@test.pl")){
                isSuccess = true;
            }
            Server.db.commit();
        }finally {
            Server.dbLock.unlock();
        }

        response.responseCode = isPasswordValid && isSuccess ? DataModel.ResponseCode.OK : DataModel.ResponseCode.FAIL;
        return response;
    }

    @NotNull
    private Response handleLogin(@NotNull Request request) throws IOException, URISyntaxException {
        Response response = new Response(null, request.requestId, null);
        String email = request.content.get("email");
        String password = request.content.get("password1");
        boolean isSuccess = false;
        Server.dbLock.lock();
        try{
            Server.db.reload();
            isSuccess = Server.db.exist(email, password);
            response.responseCode = isSuccess ? DataModel.ResponseCode.OK : DataModel.ResponseCode.FAIL;
        }finally {
            Server.dbLock.unlock();
        }

        if(isSuccess){
            addToActiveUsers(email);
        }
        return response;
    }

    private void addToActiveUsers(String email){
        Server.usersLock.lock();
        try{
            User user = Server.users.get(email);
            if(user == null){
                user = new User(email, objectInputStream, objectOutputStream);
            }else{
                user.updateStreams(objectInputStream, objectOutputStream);
            }
            user.login();
            Server.users.put(email, user);
        }finally {
            Server.usersLock.unlock();
        }
    }

    private Response handleLogout(@NotNull Request request) throws IOException, URISyntaxException {
        Response response = new Response(null, request.requestId, null);
        String email = request.content.get("email");
        String password = request.content.get("password1");
        User user = getUser(email, password);
        user.logout();
        return response;
    }

    public void closeConnection(){
        this.exit = true;
    }

    private void closeStreams() throws IOException {
        this.objectInputStream.close();
        this.objectOutputStream.close();
        this.s.close();
    }


}
