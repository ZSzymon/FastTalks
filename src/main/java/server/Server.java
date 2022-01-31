package server;

import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread
{
    private ServerSocket ss;
    private Vector<Thread> clients = new Vector<>();

    public static PrimitiveDateBase db;
    public static ReentrantLock lock;
    static {
        try {
            db = new PrimitiveDateBase("database.json");
            db.connect();
            lock = new ReentrantLock();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public Server(int port) throws IOException {
        ss = new ServerSocket(port);
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
                Thread clientThread = null;
                clientThread = new ClientHandler(socket);
                System.out.println("Adding this client to active client list");
                clients.add(clientThread);
                clientThread.start();
            }
        }
    }
}


