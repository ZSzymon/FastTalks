package server;

import java.io.IOException;

public class ServerExecutor {
    Server server;
    int port  = Config.port;
    void createServer(Boolean startOnCreation) throws IOException {
        this.server = new Server(port);
        if (startOnCreation){
            this.server.start();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerExecutor serverTest = new ServerExecutor();
        serverTest.createServer(true);
    }

}
