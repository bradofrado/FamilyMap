package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.*;

public abstract class Handler implements HttpHandler {
    private Map<String, HandlerMethod> routes = new HashMap<>();
    private HttpExchange exchange;

    /**
     * Here any child class will define methods for different routes
     */
    protected abstract void initRoutes();

    public Handler() {
        initRoutes();
    }


    /**
     * Handles the exchange
     * @param exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            this.exchange=exchange;
            String path=exchange.getRequestURI().getPath();
            String method=exchange.getRequestMethod().toLowerCase();

            //Gets the method for the given http verb and path. Defaults to /
            String key=getKey(method, path);
            if (!routes.containsKey(key)) {
                key = getKey(method, "/");
                if (!routes.containsKey(key)) {
                    sendStatus(HttpURLConnection.HTTP_NOT_FOUND);
                    return;
                }
            }

            //Get the authorization token
            Headers header=exchange.getRequestHeaders();
            String authToken=header.containsKey("Authorization") ? header.getFirst("Authorization") : null;

            //Create the request object
            HandlerMethod handlerMethod=routes.get(key);
            String body = readString(exchange.getRequestBody());
            Request request=new Request(path, authToken, body);

            handlerMethod.run(request);
        } catch (IOException ex) {
            sendStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
            ex.printStackTrace();
        }
    }

    /**
     * Sends back a message with a code to the client
     * @param rCord
     * @param message
     * @throws IOException
     */
    protected void send(int rCord, String message) throws IOException {
        this.exchange.sendResponseHeaders(rCord, 0);

        OutputStream responseBody = this.exchange.getResponseBody();
        writeString(message, responseBody);
        responseBody.close();
    }

    /**
     * Sends back a file and a code to the client
     * @param rCode
     * @param file
     * @throws IOException
     */
    protected void sendFile(int rCode, File file) throws IOException {
        this.exchange.sendResponseHeaders(rCode, 0);

        OutputStream responseBody = this.exchange.getResponseBody();
        Files.copy(file.toPath(), responseBody);

        responseBody.close();
    }

    /**
     * Sends back just a code to the client
     * @param rCord
     * @throws IOException
     */
    protected void sendStatus(int rCord) throws IOException {
        this.exchange.sendResponseHeaders(rCord, 0);
        this.exchange.getResponseBody().close();
    }

    /**
     * Registers a get method for a certain path
     * @param path The path to register
     * @param method The method to call when this path is sent
     */
    protected void get(String path, HandlerMethod method) {
        addPath("get", path, method);
    }

    /**
     * Registers a post method for a given path
     * @param path The path to register
     * @param method The method to call when this path is sent
     */
    protected void post(String path, HandlerMethod method) {
        addPath("post", path, method);
    }

    private void addPath(String method, String path, HandlerMethod handlerMethod) {
        String key = getKey(method, path);
        if (!routes.containsKey(key)) {
            routes.put(key, handlerMethod);
        } else {
            routes.replace(key, handlerMethod);
        }
    }

    private String getKey(String method, String path) {
        return method + ": " + path;
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
