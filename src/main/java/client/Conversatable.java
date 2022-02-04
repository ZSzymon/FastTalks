package client;

import utils.Message;

import java.util.Set;

public interface Conversatable {

    String toString();
    void add(Message message);
    void addAll(Set<Message> messages);
}
