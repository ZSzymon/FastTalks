package client;

import utils.Message;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Conversation implements Conversatable {


    Set<Message> messages;
    public Conversation(Message message){
        this.messages = new TreeSet<>();
        add(message);
    }
    public Conversation(Set<Message> messages){
        this.messages = new TreeSet<>();
        addAll(messages);
    }

    @Override
    public void add(Message message) {
        messages.add(message);
    }

    @Override
    public void addAll(Set<Message> messages) {
        messages.forEach(this::add);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(Message message: this.messages) {
            stringBuilder.append(message.creationDateHuman).append("\n");
            stringBuilder.append(message.senderEmail).append(": to : ").append(message.receiverEmail).append("\n");
            stringBuilder.append(message.content).append("\n");
        }
        return stringBuilder.toString();

    }

    public List<String> toStringList(){
        List<String> strings = this.messages.stream()
                .map(this::messageToString)
                .collect(Collectors.toList());
        return strings;

    }
    private String messageToString(Message message){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message.creationDateHuman).append("\n");
        stringBuilder.append(message.senderEmail).append(": to : ").append(message.receiverEmail).append("\n");
        stringBuilder.append(message.content).append("\n");
        return stringBuilder.toString();

    }

}
