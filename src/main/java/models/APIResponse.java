package models;

public class APIResponse {
    private int statusCode;
    private String message;
    private String status;
    private Object body;

    public APIResponse(int statusCode, String message, String status, Object body) {
        this.statusCode = statusCode;
        this.message = message;
        this.status = status;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public Object getBody() {
        return body;
    }
}
