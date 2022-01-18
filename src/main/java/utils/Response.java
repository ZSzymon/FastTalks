package utils;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;



public class Response extends DataModel implements Serializable {
    public UUID responseId;
    public ResponseCode responseCode;

    public Response(Map<String, String>content, UUID responseId, ResponseCode responseCode) {
        super(content);
        this.responseId = responseId;
        this.responseCode = responseCode;
    }

    public Response(){
        super();
    }
    @Override
    public String toString() {
        return "Response{" +
                "responseId=" + responseId +
                ", responseCode=" + responseCode +
                ", creationDate='" + creationDate + '\'' +
                '}';
    }
}