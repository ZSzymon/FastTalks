package server;

import utils.Request;
import utils.Response;

import java.io.IOException;
import java.util.Set;

public class Responser extends Thread {
    boolean exit;
    Responser(){
        this.exit = false;
    }
    public void run(){
        while (!exit)
        try{
            ClientHandler clientHandler = Server.responsesQueue.take();
            if(clientHandler != null){
                clientHandler.sendData(clientHandler.responses);

            }
        }catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
