package utils;


import java.io.Serializable;
import java.util.Map;

public class Request extends DataModel implements Serializable {
    public int requestId;
    public RequestType requestType;

    public Request(Map<String, String>content, int requestId, RequestType requestType) {
        super(content);
        this.requestId = requestId;
        this.requestType = requestType;
    }

//    @NotNull
//    private static Request registerRequest(String email, String password1, String password2, int requestId){
//        Map<String, String> hashMap = new HashMap<>();
//        hashMap.put("email", email);
//        hashMap.put("password1", password1);
//        hashMap.put("password2", password2);
//        Request request = new Request(hashMap,requestId, RequestType.REGISTER);
//        return request;
//    }
//    @NotNull
//    private static Request loginRequest(String email, String password, int requestId){
//        Map<String, String> hashMap = new HashMap<>();
//        hashMap.put("email", email);
//        hashMap.put("password", password);
//        Request request = new Request(hashMap,requestId, RequestType.LOGIN);
//        return request;
//    }
//
//    @NotNull
//    private static Request logoutRequest(String email, String password, int requestId){
//        Map<String, String> hashMap = new HashMap<>();
//        hashMap.put("email", email);
//        hashMap.put("password", password);
//        Request request = new Request(hashMap,requestId, RequestType.LOGOUT);
//        return request;
//    }

}