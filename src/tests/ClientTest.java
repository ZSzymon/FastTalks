package tests;
import client.Client;
import org.junit.jupiter.api.Test;
import utils.DataModel;
import utils.Request;
import utils.Response;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


class ClientTest {
    int port = Config.port;
    Thread clientThread;
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
    void testCreateRequest(){
        Request request = new Request(new HashMap<>(), 0, DataModel.RequestType.REGISTER);
        assertEquals(request.requestId, 0);
        assertEquals(request.requestType, DataModel.RequestType.REGISTER);
    }

    @Test
    void testSendRequest() throws InterruptedException {
        int requestId = 0;
        Request request = new Request(new HashMap<>(), requestId, DataModel.RequestType.REGISTER);
        this.client.addRequest(request);
        this.client.startListener();
        Thread.sleep(1);
        Response response = this.client.messageListener.getResponse();

        System.out.println(response);
    }

    @Test
    void testSendLotOfRequests() throws InterruptedException {
        int requestId = 0;
        Request request = new Request(new HashMap<>(), requestId, DataModel.RequestType.REGISTER);
        this.client.addRequest(request);
        this.client.startListener();
        Thread.sleep(1000);
        for (int i = 1; i < 10 ; i++) {
            requestId = i;
            request = new Request(new HashMap<>(), requestId, DataModel.RequestType.REGISTER);
            this.client.addRequest(request);
            Response response = this.client.messageListener.getResponse();
            System.out.println(response);
        }
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