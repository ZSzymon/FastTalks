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

    void createClient(Boolean startOnCreation) throws InterruptedException {
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
        this.client.startSender();

        this.client.startReceiver();
        Thread.sleep(500);
        Response response = this.client.getResponse(requestId);
        System.out.println(response);
    }

    @Test
    void testSendLotOfRequest() throws IOException, ClassNotFoundException, InterruptedException{

        for (int i = 0; i <10 ; i++) {
            this.client.connect();
            int requestId = i;
            Request request = new Request(new HashMap<>(), requestId, DataModel.RequestType.REGISTER);
            this.client.addRequest(request);
            this.client.destroy();
        }
        //Response response = this.client.getResponse(requestId);
        //System.out.println(response);

    }
    @Test
    void testTwoRequestSameClient() throws InterruptedException {
        Client client1 = new Client("localhost", Config.port);
        Request request1 = new Request(null, 1, DataModel.RequestType.REGISTER);
        Request request2 = new Request(null, 2, DataModel.RequestType.REGISTER);
        Request request3 = new Request(null, 3, DataModel.RequestType.REGISTER);
        client1.addRequest(request1);
        client1.addRequest(request2);
        client1.addRequest(request3);
        Thread.sleep(100);
        Response response1 = client1.getResponse(1);
        //assertNotNull(response1);
        //assertNotNull(client1.getResponse(2));
    }
//    @Test
//    void testTwoClients() throws IOException, ClassNotFoundException, InterruptedException {
//        Client client1 = new Client("localhost", Config.port);
//        Client client2 = new Client("localhost", Config.port);
//
//        Request request1 = new Request(null, 1, DataModel.RequestType.REGISTER);
//        Request request11 = new Request(null, 11, DataModel.RequestType.REGISTER);
//        Request request2 = new Request(null, 2, DataModel.RequestType.REGISTER);
//        Request request22 = new Request(null, 22, DataModel.RequestType.REGISTER);
//
//        client1.addRequest(request1);
//        client1.addRequest(request11);
//        client2.addRequest(request2);
//        client2.addRequest(request22);
//
//        clientThread1.start();
//        clientThread2.start();
//        //client1.connect();
//        //client1.sendAll();
//        Thread.sleep(1000);
//        Response response1 = client1.getResponse(1);
//        Response response11 = client1.getResponse(111);
//        Response response2 = client2.getResponse(2);
//        Response response22 = client2.getResponse(22);
//
//        System.out.println(response1);
//        System.out.println(response11);
//        System.out.println(response2);
//
//        System.out.println(response22);
//
//
//    }


    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void testSetUp(){

    }
}