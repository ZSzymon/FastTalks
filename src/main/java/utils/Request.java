package utils;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Request extends DataModel implements Serializable {
    public UUID requestId;
    public DataModel.RequestType requestType;
    public String email;


    public Request(Map<String, String>content, UUID requestId, DataModel.RequestType requestType) {
        super(content);
        this.requestId = requestId;
        this.requestType = requestType;
        this.email = null;
    }
    public Request(String email, Map<String, String>content, UUID requestId, RequestType requestType) {
        super(content);
        this.email = email;
        this.requestId = requestId;
        this.requestType = requestType;
    }

    private static Request messageRequest(UUID requestId, String senderEmail, String receiverMail, String message){
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("sender", senderEmail);
        hashMap.put("receiver", receiverMail);
        hashMap.put("message", message);
        Request request = new Request(senderEmail, hashMap, requestId, RequestType.CHAT_MESSAGE);
        return request;
    }
    private static Request heartbeatRequest(UUID requestId){
        Request request = new Request(null, requestId, RequestType.HEARTBEAT);
        return request;
    }
    @NotNull
    private static Request registerRequest(String email, String password1, String password2, UUID requestId){
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("password1", password1);
        hashMap.put("password2", password2);
        Request request = new Request(email, hashMap, requestId, RequestType.REGISTER);
        return request;
    }
    @NotNull
    private static Request loginRequest(String email, String password, UUID requestId){
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("password", password);
        Request request = new Request(email, hashMap, requestId, RequestType.LOGIN);
        return request;
    }

    @NotNull
    private static Request logoutRequest(String email, String password, UUID requestId){
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("password", password);
        Request request = new Request(email, hashMap,requestId, RequestType.LOGOUT);
        return request;
   }

}