package tests;
import client.Client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import utils.DataModel;
import utils.Request;
import utils.Response;

import java.io.IOException;
import java.lang.reflect.Type;
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

    @Test
    void testToJson(){
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map.put("dupa", "3");
        System.out.println(gson.toJson(map));
    }

    @Test
    void testFromJsonToMap(){
        //GIVEN
        Gson gson = new Gson();
        Map<String, String> exampleMap = new HashMap<>();
        exampleMap.put("dupa", "3");
        Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
        String json = gson.toJson(exampleMap,mapType);
        //WHEN
        Map<String, String> exampleMapDeserialize  = gson.fromJson(json, mapType);
        //THEN
        assertEquals(exampleMap, exampleMapDeserialize);
    }


    void testCreateRequest(){
        Request request = new Request(new HashMap<>(), UUID.randomUUID(), DataModel.RequestType.REGISTER);
        assertEquals(request.requestId, 0);
        assertEquals(request.requestType, DataModel.RequestType.REGISTER);
    }

    @Test
    void testSendRequest() throws InterruptedException {
        UUID requestId = UUID.randomUUID();
        Request request = new Request(new HashMap<>(), requestId, DataModel.RequestType.REGISTER);
        this.client.addRequest(request);
        this.client.startListener();
        Thread.sleep(1);
        Response response = this.client.messageListener.getResponse();

        System.out.println(response);
    }

    @Test
    void testSendLotOfRequests() throws InterruptedException {
        UUID requestId = UUID.randomUUID();
        Request request = new Request(new HashMap<>(), requestId, DataModel.RequestType.REGISTER);
        this.client.addRequest(request);

        for (int i = 1; i < 10 ; i++) {
            requestId = UUID.randomUUID();
            request = new Request(new HashMap<>(), requestId, DataModel.RequestType.REGISTER);
            Thread.sleep(500);
            this.client.addRequest(request);
        }
        this.client.startListener();
        Response response = this.client.messageListener.getResponse();
        System.out.println(response);
    }



    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testSetUp(){

    }
}