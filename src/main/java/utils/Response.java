package utils;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;



public class Response extends DataModel implements Serializable {
    public UUID responseId;
    ResponseCode responseCode;

    public Response(Map<String, String>content, UUID responseId, ResponseCode responseCode) {
        super(content);
        this.responseId = responseId;
        this.responseCode = responseCode;
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