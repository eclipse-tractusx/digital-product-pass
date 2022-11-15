package net.catenax.ce.materialpass.http.models;

import java.util.Map;

public class Response {

    public String message;
    public Integer status;
    public String statusText;

    public Response(String message, Integer status, String statusText) {
        this.message = message;
        this.status = status;
        this.statusText = statusText;
    }

    public Response() {
    }

    public Response mapError(Map<String, Object> errorAttributes) {
        this.message = errorAttributes.getOrDefault("message", "An error occurred in the server").toString();
        this.status = (Integer) errorAttributes.getOrDefault("status", 500);
        this.statusText = errorAttributes.getOrDefault("error", "Internal error").toString();
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String errorString(){
        return "["+this.status+" "+this.statusText+"]: "+this.message;
    }

}
