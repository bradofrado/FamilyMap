package handlers;

public class Request {
    private String authToken;
    private String body;
    private String path;

    public Request(String path, String authToken, String body) {
        this.authToken = authToken;
        this.body = body;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getBody() {
        return body;
    }
}
