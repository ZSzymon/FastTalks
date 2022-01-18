package server;

import client.Client;
import utils.PrimitiveDateBase;
import utils.Request;

import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;

// Server class
public class Server extends Thread
{

    ServerSocket ss;

    static public Vector<ClientHandler> clientsThreads;
    //Use for fast finding users by mail.
    static public Map<String, ClientHandler> clientsMap = new HashMap<>();
    //Used by RequestAnalizator for responding
    static public ArrayBlockingQueue<ClientHandler> requestQueue = new ArrayBlockingQueue<>(200);
    static public PrimitiveDateBase primitiveDateBase;

    public Server(int port) throws IOException, URISyntaxException {
        clientsThreads = new Vector<>();
        ss = new ServerSocket(port);
        initDatebase();
    }
    private void initDatebase() throws URISyntaxException, IOException {
        primitiveDateBase = new PrimitiveDateBase("users.json");
        primitiveDateBase.connect();
    }


    @Override
    public void run() {
        Socket s;
        while (true)
        {
            Socket socket = null;
            // Accept the incoming request
            try {
                socket = ss.accept();
                System.out.println("New client request received : " + socket);
                System.out.println("Creating a new handler for this client...");

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(socket != null){
                ClientHandler clientThread = null;
                clientThread = new ClientHandler(socket);
                System.out.println("Adding this client to active client list");
                clientsThreads.add(clientThread);
                clientThread.start();
            }

        }
    }
}


