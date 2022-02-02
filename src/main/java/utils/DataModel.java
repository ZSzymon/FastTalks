package utils;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public class DataModel implements Serializable{

    public Map<String, String> content;
    public String creationDate;
    public DataModel(Map<String, String> content) {
        this.content = content;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.creationDate = dtf.format(now);
    }

    public enum RequestType{
        REGISTER, LOGIN, LOGOUT, CHAT_MESSAGE, HEARTBEAT, DOWNLOAD_MESSAGES
    }
    public enum ResponseCode{
        NONE, OK, FAIL, RECEIVER_NOT_FOUND
    }
    public static UUID generateUUID(){
        return UUID.randomUUID();
    }

}

