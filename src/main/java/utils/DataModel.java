package utils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public class DataModel implements Serializable{

    Map<String, String> content;
    String creationDate;
    public DataModel(Map<String, String> content) {
        this.content = content;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.creationDate = dtf.format(now);
    }

    public enum RequestType{
        REGISTER, LOGIN, LOGOUT, CHAT_MESSAGE, HEARTBEAT
    }
    public enum ResponseCode{
        NONE, OK, FAIL
    }
    public UUID getUUID(){
        return UUID.randomUUID();
    }

}

