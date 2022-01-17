package server;

import java.io.*;
import java.util.*;
import java.net.*;
// Server class
public class Server extends Thread
{
    ServerSocket ss;
    public Vector<Thread> clients = new Vector<>();
    public File clientsFile = new File("");
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
                clientThread = new Thread(new ClientHandler(socket));
                System.out.println("Adding this client to active client list");
                clients.add(clientThread);
                clientThread.start();
            }

        }
    }
}


