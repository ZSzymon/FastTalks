package tests;
import client.Client;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import utils.DataModel;
import utils.Message;
import utils.Request;
import utils.Response;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class ClientTest {
    int port = Config.port;
    Client client;


    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException, InterruptedException {
        createClient(true);
    }

    void createClient(Boolean startOnCreation) throws InterruptedException, IOException {
        this.client = new Client("localhost", this.port);
        if (startOnCreation){
            this.client.connect();
        }
    }




    void testCreateRequest(){
        Request request = new Request(new HashMap<>(), UUID.randomUUID(), DataModel.RequestType.HEARTBEAT);
        assertEquals(request.requestId, 0);
        assertEquals(request.requestType, DataModel.RequestType.REGISTER);
    }

    @Test
    void testSendRequest() throws InterruptedException, IOException {
        UUID requestId = UUID.randomUUID();
        Request request = new Request(new HashMap<>(), requestId, DataModel.RequestType.HEARTBEAT);
        this.client.addRequest(request);
        this.client.startListener();
        Thread.sleep(100);
        Response response = this.client.receiver.getAnyResponse();

        System.out.println(response);
    }

    @Test
    void testSendLotOfRequests() throws InterruptedException, IOException {
        UUID requestId = UUID.randomUUID();
        Request request = new Request(new HashMap<>(), requestId, DataModel.RequestType.HEARTBEAT);
        this.client.addRequest(request);

        for (int i = 1; i < 10 ; i++) {
            requestId = UUID.randomUUID();
            request = new Request(new HashMap<>(), requestId, DataModel.RequestType.HEARTBEAT);

            this.client.addRequest(request);
        }
        this.client.startListener();
        Thread.sleep(500);
        Response response = this.client.receiver.getResponse(requestId);
        System.out.println(response);
    }
    @Test
    void test(){
        //GIVEN
        //WHEN
        //THEN
    }
    public Pair<String, String> defaultUser(){
        return new Pair("test@gmail.com", "password");
    }
    void registerUser(String email, String password) throws InterruptedException, IOException {

        UUID requestId = UUID.randomUUID();
        HashMap<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password1", password);
        payload.put("password2", password);
        Request request = new Request(payload, requestId, DataModel.RequestType.REGISTER);
        this.client.addRequest(request);
        this.client.startListener();
        Thread.sleep(500);
    }
    @Test
    void registerAndloginDefaultUser() throws InterruptedException, IOException {
        UUID registerRequestId = UUID.randomUUID();
        UUID loginRequestId = UUID.randomUUID();
        HashMap<String, String> payload = new HashMap<>();
        Pair<String, String> defaultUser = this.defaultUser();
        payload.put("email", defaultUser.getKey());
        payload.put("password1", defaultUser.getValue());
        payload.put("password2", defaultUser.getValue());
        Request RegisterRequest = new Request(payload, registerRequestId, DataModel.RequestType.REGISTER);
        Request LoginRequest = new Request(payload, loginRequestId, DataModel.RequestType.LOGIN);
        this.client.addRequest(RegisterRequest);
        this.client.addRequest(LoginRequest);
        this.client.startListener();
        Thread.sleep(500);
        Response LoginResponse = waitForResponse(loginRequestId);
        Response registerResponse = waitForResponse(loginRequestId);
        assertEquals(LoginResponse.responseCode, DataModel.ResponseCode.OK);
    }
    @Test
    void registerLoginLogoutRequests() throws InterruptedException, IOException {
        UUID registerRequestId = UUID.randomUUID();
        UUID loginRequestId = UUID.randomUUID();
        UUID logoutRequestId = UUID.randomUUID();
        HashMap<String, String> payload = new HashMap<>();
        Pair<String, String> defaultUser = this.defaultUser();
        payload.put("email", defaultUser.getKey());
        payload.put("password1", defaultUser.getValue());
        payload.put("password2", defaultUser.getValue());
        Request registerRequest = new Request(defaultUser.getKey(), payload, registerRequestId, DataModel.RequestType.REGISTER);
        Request loginRequest = new Request(defaultUser.getKey(), payload, loginRequestId, DataModel.RequestType.LOGIN);
        Request logoutRequest = new Request(defaultUser.getKey(), payload, logoutRequestId, DataModel.RequestType.LOGOUT);
        this.client.startListener();
        this.client.addRequest(registerRequest);
        this.client.addRequest(loginRequest);
        this.client.addRequest(logoutRequest);


        Response responseRegister = waitForResponse(registerRequestId);
        Response responseLogin = waitForResponse(loginRequestId);
        Response responseLogout = waitForResponse(logoutRequestId);




    }
    void registerAndloginDefaultUserNotTest() throws InterruptedException, IOException {
        UUID registerRequestId = UUID.randomUUID();
        UUID loginRequestId = UUID.randomUUID();
        HashMap<String, String> payload = new HashMap<>();
        Pair<String, String> defaultUser = this.defaultUser();
        payload.put("email", defaultUser.getKey());
        payload.put("password1", defaultUser.getValue());
        payload.put("password2", defaultUser.getValue());
        Request RegisterRequest = new Request(payload, registerRequestId, DataModel.RequestType.REGISTER);
        Request LoginRequest = new Request(payload, loginRequestId, DataModel.RequestType.LOGIN);
        this.client.addRequest(RegisterRequest);
        this.client.addRequest(LoginRequest);
        this.client.startListener();
        Thread.sleep(500);
        Response LoginResponse = waitForResponse(loginRequestId);
        Response registerResponse = waitForResponse(loginRequestId);
        assertEquals(LoginResponse.responseCode, DataModel.ResponseCode.OK);
    }

    @Test
    void testLogin() throws IOException, InterruptedException {
        //GIVEN
        Pair<String, String> defaultUser = this.defaultUser();
        registerUser(defaultUser.getKey(), defaultUser.getValue());
        //WHEN
        HashMap<String, String> payload = new HashMap<>();
        payload.put("email", defaultUser.getKey());
        payload.put("password1", defaultUser.getValue());
        UUID loginRequestUUID = UUID.randomUUID();
        Request request = new Request(payload, loginRequestUUID, DataModel.RequestType.LOGIN);
        this.client.startListener();
        this.client.addRequest(request);
        Thread.sleep(500);
        //THEN
        Response response = this.client.receiver.getResponse(loginRequestUUID);
        System.out.println(response);
        assertEquals(response.responseCode, DataModel.ResponseCode.OK);
    }


    @Test
    void testLogout() throws IOException, InterruptedException {
        //GIVEN
        Pair<String, String> defaultUser = this.defaultUser();
        registerAndloginDefaultUserNotTest();
        //WHEN
        HashMap<String, String> payload = new HashMap<>();
        payload.put("email", defaultUser.getKey());
        UUID requestUUID = UUID.randomUUID();
        Request request = new Request(payload, requestUUID, DataModel.RequestType.LOGOUT);
        this.client.startListener();
        this.client.addRequest(request);
        Thread.sleep(200);
        //THEN
        Response response = this.client.receiver.getResponse(requestUUID);
        System.out.println(response);
        assertEquals(DataModel.ResponseCode.OK, response.responseCode);
    }

    @Test
    void testLogoutFail() throws IOException, InterruptedException {
        //GIVEN
        Pair<String, String> defaultUser = this.defaultUser();
        //WHEN
        HashMap<String, String> payload = new HashMap<>();
        payload.put("email", defaultUser.getKey()+"gregregre");
        UUID requestUUID = UUID.randomUUID();
        Request request = new Request(payload, requestUUID, DataModel.RequestType.LOGOUT);
        this.client.startListener();
        this.client.addRequest(request);
        Thread.sleep(200);
        //THEN
        Response response = this.client.receiver.getResponse(requestUUID);
        System.out.println(response);
        assertEquals(DataModel.ResponseCode.FAIL, response.responseCode);
    }


    @Test
    void testRegister() throws InterruptedException, IOException {
        UUID requestId = UUID.randomUUID();
        HashMap<String, String> payload = new HashMap<>();
        payload.put("email", "szymon@test.pl");
        payload.put("password1", "test");
        payload.put("password2", "test");
        Request request = new Request(payload, requestId, DataModel.RequestType.REGISTER);

        this.client.startListener();
        this.client.addRequest(request);
        Thread.sleep(500);

        Response response = this.client.receiver.getResponse(requestId);
        System.out.println(response);
        assertEquals(response.responseCode, DataModel.ResponseCode.OK);
    }

    @Test
    void testMessageSuccess() throws InterruptedException, IOException {
        registerAndloginDefaultUser();
        String email = defaultUser().getKey();
        String password = defaultUser().getValue();
        UUID requestId = UUID.randomUUID();
        HashMap<String, String> payload = new HashMap<>();
        payload.put("email", email);
        String messageString = new Message(email, email, "Hello World").toJson();
        payload.put("message", messageString);
        Request request = new Request(email,payload, requestId, DataModel.RequestType.CHAT_MESSAGE);
        Response response = sendRequestGetResponse(request);
        System.out.println(response);
        assertEquals(response.responseCode, DataModel.ResponseCode.OK);
    }

    @Test
    void testMessageFail() throws InterruptedException, IOException {
        registerAndloginDefaultUser();
        String email = defaultUser().getKey();
        String password = defaultUser().getValue();
        UUID requestId = UUID.randomUUID();
        HashMap<String, String> payload = new HashMap<>();
        payload.put("email", email);
        String messageString = new Message(email, email+"NOTEXIST.", "Hello World").toJson();
        payload.put("message", messageString);
        Request request = new Request(email,payload, requestId, DataModel.RequestType.CHAT_MESSAGE);
        Response response = sendRequestGetResponse(request);
        System.out.println(response);
        assertEquals(response.responseCode, DataModel.ResponseCode.FAIL);
    }

    void sendHelloMessage(String receiver) throws IOException, InterruptedException {
        registerAndloginDefaultUser();
        String email = defaultUser().getKey();
        UUID requestId = UUID.randomUUID();
        HashMap<String, String> payload = new HashMap<>();
        payload.put("email", email);
        String messageString = new Message(email, receiver, "Hello World").toJson();
        payload.put("message", messageString);
        Request request = new Request(email, payload, requestId, DataModel.RequestType.CHAT_MESSAGE);
        Response response = sendRequestGetResponse(request);
    }
    @Test
    void testDownloadMessages() throws IOException, InterruptedException {
        String receiver = defaultUser().getKey();
        String sender = defaultUser().getKey();
        sendHelloMessage(receiver);
        UUID requestId = UUID.randomUUID();
        Request request = new Request(sender,null, requestId, DataModel.RequestType.DOWNLOAD_MESSAGES);
        Response response =  sendRequestGetResponse(request);

        assertEquals(response.responseCode, DataModel.ResponseCode.OK);
    }

    private Response waitForResponse(UUID requestId) throws InterruptedException {
        Response response = null;
        while (response == null){
            response = this.client.receiver.getResponse(requestId);
            Thread.sleep(10);
        }
        return response;
    }
    Response sendRequestGetResponse(Request request) throws InterruptedException, IOException {
        this.client.addRequest(request);
        this.client.startListener();
        Response response = waitForResponse(request.requestId);
        return response;
    }
    @Test
    void testRegisterFail() throws InterruptedException, IOException {
        UUID requestId = UUID.randomUUID();
        HashMap<String, String> payload = new HashMap<>();
        String email = "szymon@test2.pl";
        payload.put("email", email);
        payload.put("password1", "test");
        payload.put("password2", "test");
        Request request = new Request(email, payload, requestId, DataModel.RequestType.REGISTER);
        this.client.addRequest(request);
        this.client.startListener();
        Thread.sleep(500);

        Response response = this.client.receiver.getResponse(requestId);
        System.out.println(response);
        assertEquals(response.responseCode, DataModel.ResponseCode.FAIL);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testSetUp(){

    }
}