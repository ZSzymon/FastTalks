package server;

import server.ClientHandler;
import server.Server;
import utils.DataModel;
import utils.PasswordValidator;
import utils.Request;
import utils.Response;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import utils.DataModel.*;

public class RequestAnalyser extends Thread {
    private ClientHandler clientHandler;
    RequestAnalyser(){
        super("RequestAnalyzer thread");
    }
    public void run(){
            try{
                clientHandler = Server.requestQueue.take();
                if(clientHandler != null){
                    Set<Request> requests = clientHandler.requests;
                    Set<Response> responses = handleRequests(requests);
                    clientHandler.responses = responses;
                    Server.responsesQueue.add(clientHandler);
                }else{
                    if (Server.responser.getState() == State.WAITING){
                        synchronized (Server.responser){
                            Server.responser.notify();
                        }
                    }
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
    public Set<Response> handleRequests(Set<Request> requests){
        Response response;
        Set<Response> responses = new HashSet<>();
        for(Request request: requests){
            response = handleRequest(request);
            responses.add(response);
        }
        return responses;
    }
    private Response handleRequest(Request request) {
        String senderEmail = request.email;
        Response response = new Response();
        if(!Server.clientsMap.containsKey(senderEmail)){
            Server.clientsMap.put(senderEmail, clientHandler);
        }

        if (request.requestType == DataModel.RequestType.REGISTER) {
            Map<String, String> content = request.content;
            String login = content.get("email");
            String password1 = content.get("password1");
            String password2 = content.get("password2");
            Boolean isRegisteredAlready = Server.primitiveDateBase.exist(login);
            if(isRegisteredAlready){
                response.responseCode = ResponseCode.FAIL;
                response.content.put("details", "Email: <"+ login + "> already exists");
                response.responseId = request.requestId;
            }else{
                PasswordValidator passwordValidator = new PasswordValidator(password1, password2);
                if(passwordValidator.isPasswordValid()){
                    Server.primitiveDateBase.addUser(password1, password2);
                    response.responseCode = ResponseCode.OK;
                }else {
                    response.responseCode = ResponseCode.FAIL;
                    response.content.put("details", "Password not valid.");
                }

            }

        } else if (request.requestType == DataModel.RequestType.LOGIN) {

        } else if (request.requestType == DataModel.RequestType.LOGOUT) {

        } else if (request.requestType == DataModel.RequestType.CHAT_MESSAGE) {

        } else if (request.requestType == DataModel.RequestType.HEARTBEAT){
            return new Response();
        }
        return response;
    }

}
