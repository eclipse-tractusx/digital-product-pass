package net.catenax.ce.materialpass.http.models;

import java.util.Map;

public class Response {

    public String message = null;
    public Integer status = 200;
    public String statusText = "Success";

    public Object data = null;

    public Response(String message, Integer status, String statusText) {
        this.message = message;
        this.status = status;
        this.statusText = statusText;
    }

    public Response(String message, Integer status, String statusText, Object data) {
        this.message = message;
        this.status = status;
        this.statusText = statusText;
        this.data = data;
    }

    public Response(String message, Integer status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
