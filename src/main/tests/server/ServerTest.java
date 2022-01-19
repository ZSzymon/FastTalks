package server;

import java.io.IOException;
import java.net.URISyntaxException;

public class ServerTest {
    Server server;
    int port  = Config.port;
    void runServer(){
        this.server.start();
    }
    void createServer(Boolean startOnCreation) throws IOException, URISyntaxException {
        this.server = new Server(port, true);
        if (startOnCreation){
            this.server.start();
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerTest serverTest = new ServerTest();
        serverTest.createServer(true);
    }
}
