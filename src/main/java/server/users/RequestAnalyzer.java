package server.users;

import server.ClientHandler;
import server.Server;
import utils.DataModel;
import utils.Request;
import utils.Response;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import utils.DataModel.*;

public class RequestAnalyzer extends Thread {
    ClientHandler clientHandler;
    public void run(){
        while(true){
            try{
                clientHandler = Server.requestQueue.take();
                Set<Request> requests = clientHandler.requests;
                Set<Response> responses = handleRequests(requests);
                clientHandler.objectOutputStream.writeObject(responses);
                clientHandler.objectOutputStream.flush();

            }catch (Exception e){

            }
        }

    }
    private Set<Response> handleRequests(Set<Request> requests){
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
            String login = content.get("login");
            String password1 = content.get("password1");
            String password2 = content.get("password2");
            Boolean isRegisteredAlready = Server.primitiveDateBase.exist(login);
            if(isRegisteredAlready){
                response.responseCode = ResponseCode.FAIL;

            }
        } else if (request.requestType == DataModel.RequestType.LOGIN) {

        } else if (request.requestType == DataModel.RequestType.LOGOUT) {

        } else if (request.requestType == DataModel.RequestType.CHAT_MESSAGE) {

        } else if (request.requestType == DataModel.RequestType.HEARTBEAT){

        }
        return new Response(null, request.requestId, DataModel.ResponseCode.OK);
    }

}
