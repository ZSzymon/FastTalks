package tests;

import org.jetbrains.annotations.NotNull;
import server.Server;

import java.io.IOException;

public class ServerTest {
    Server server;
    int port  = Config.port;
    void runServer(){
        this.server.start();
    }
    void createServer(@NotNull Boolean startOnCreation) throws IOException {
        this.server = new Server(port);
        if (startOnCreation){
            this.server.start();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerTest serverTest = new ServerTest();
        serverTest.createServer(true);
    }
}
