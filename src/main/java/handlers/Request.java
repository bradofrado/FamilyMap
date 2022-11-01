package handlers;

import java.util.Map;

/**
 * A generic request object that holds necessary data for the handlers
 */
public class Request {
    private String authToken;
    private String body;
    private String path;
    private Map<String, String> parameters;

    public Request(String path, String authToken, String body) {
        this(path, authToken, body, null);
    }

    public Request(String path, String authToken, String body, Map<String, String> parameters) {
        this.authToken = authToken;
        this.body = body;
        this.path = path;
        this.parameters = parameters;
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

    public Map<String, String> getParameters() {
        return parameters;
    }
}
