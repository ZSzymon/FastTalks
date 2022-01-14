package utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class Response extends DataModel implements Serializable {
    public int responseId;
    ResponseCode responseCode;

    public Response(Map<String, String>content, int responseId, ResponseCode responseCode) {
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