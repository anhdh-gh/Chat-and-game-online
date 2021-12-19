package chat.common;

import chat.enumeration.RequestType;
import chat.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResult implements Serializable {
    static final long serialVersionUID = 4L;

    private Object data;
    private Status status;
    private String message;
    private RequestType requestType;

    public ServiceResult(RequestType requestType) {
        this.requestType = requestType;
    }

    public ServiceResult(Object data, RequestType requestType) {
        this.data = data;
        this.requestType = requestType;
    }

    public ServiceResult(Status status, String message, RequestType requestType) {
        this.status = status;
        this.message = message;
        this.requestType = requestType;
    }
}