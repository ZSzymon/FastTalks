package utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Message {
    public String senderEmail;
    public String receiverEmail;
    public String content;
    public LocalDateTime creationDate;
    public String creationDateHuman;
    public UUID uuid;

    public Message(String senderEmail, String receiverEmail, String content) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.content = content;
        this.uuid = UUID.randomUUID();
        initDateTime();
    }


    private void initDateTime(){
        this.creationDate = LocalDateTime.now();
        this.creationDateHuman = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(creationDate);
    }

    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this, this.getClass());
        return json;
    }

    public static Message fromJson(String json){
        Gson gson = new Gson();
        Message message = gson.fromJson(json, Message.class);
        return message;
    }
    public static String collectionToJson(Collection<Message> messages){
        Gson gson = new Gson();
        String json = gson.toJson(messages, messages.getClass());
        return json;
    }
    public static Collection<Message> fromJson(String json, Type type){
        Gson gson = new Gson();
        Collection<Message> messages = gson.fromJson(json, type);
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return senderEmail.equals(message.senderEmail) &&
                receiverEmail.equals(message.receiverEmail) &&
                content.equals(message.content) &&
                creationDate.equals(message.creationDate) &&
                Objects.equals(uuid, message.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderEmail, receiverEmail, content, creationDate, uuid);
    }
}
