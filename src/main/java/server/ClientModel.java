package server;

import client.Client;
import utils.Request;

import java.util.Set;

public class ClientModel {
    String email;
    Set<Request> messagesToClient;

    ClientModel(String email){
        this.email = email;
    }
}
