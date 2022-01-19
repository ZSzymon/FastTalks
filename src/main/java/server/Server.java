package server;

import utils.PrimitiveDateBase;
import utils.Response;

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
    static public Map<String, ClientHandler> clientsMap;
    //Used by RequestAnalizator for responding
    static public ArrayBlockingQueue<ClientHandler> requestQueue;
    static public ArrayBlockingQueue<ClientHandler> responsesQueue;

    static public PrimitiveDateBase primitiveDateBase;
    static public final RequestAnalyser requestAnalyser = new RequestAnalyser();
    static public final Responser responser = new Responser();

    public Server(int port, boolean testMode) throws IOException, URISyntaxException {
        clientsThreads = new Vector<>();
        ss = new ServerSocket(port);
        initDatebase();
        requestQueue = new ArrayBlockingQueue<>(200);
        responsesQueue = new ArrayBlockingQueue<>(200);
        clientsMap = new HashMap<>();
        if (testMode){
            primitiveDateBase = new PrimitiveDateBase("usertests.json");
            primitiveDateBase.connect();
            primitiveDateBase.cleanFile();
        }
    }

    public Server(int port) throws IOException, URISyntaxException {
        clientsThreads = new Vector<>();
        ss = new ServerSocket(port);
        initDatebase();
        requestQueue = new ArrayBlockingQueue<>(200);
        responsesQueue = new ArrayBlockingQueue<>(200);
        clientsMap = new HashMap<>();
        primitiveDateBase = new PrimitiveDateBase("users.json");
        primitiveDateBase.connect();

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
                clientThread = new ClientHandler(socket, clientsThreads.size()+1);
                System.out.println("Adding this client to active client list");
                clientsThreads.add(clientThread);
                if(!requestAnalyser.isAlive()){
                    requestAnalyser.start();
                }
                if(!responser.isAlive()){
                    responser.start();
                }
                clientThread.start();
            }

        }
    }
}


