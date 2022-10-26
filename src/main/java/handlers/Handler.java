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

    protected abstract void initRoutes();

    public Handler() {
        initRoutes();
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            this.exchange=exchange;
            String path=exchange.getRequestURI().getPath();
            String method=exchange.getRequestMethod().toLowerCase();

            String key=getKey(method, path);
            if (!routes.containsKey(key)) {
                key = getKey(method, "/");
                if (!routes.containsKey(key)) {
                    sendStatus(HttpURLConnection.HTTP_NOT_FOUND);
                    return;
                }
            }

            Headers header=exchange.getRequestHeaders();
            String authToken=header.containsKey("Authorization") ? header.getFirst("Authorization") : null;

            HandlerMethod handlerMethod=routes.get(key);
            String body = readString(exchange.getRequestBody());
            Request request=new Request(path, authToken, body);

            handlerMethod.run(request);
        } catch (IOException ex) {
            sendStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
            ex.printStackTrace();
        }
    }

    protected void send(int rCord, String message) throws IOException {
        this.exchange.sendResponseHeaders(rCord, 0);

        OutputStream responseBody = this.exchange.getResponseBody();
        writeString(message, responseBody);
        responseBody.close();
    }

    protected void sendFile(int rCode, File file) throws IOException {
        this.exchange.sendResponseHeaders(rCode, 0);

        OutputStream responseBody = this.exchange.getResponseBody();
        Files.copy(file.toPath(), responseBody);

        responseBody.close();
    }

    protected void sendStatus(int rCord) throws IOException {
        this.exchange.sendResponseHeaders(rCord, 0);
        this.exchange.getResponseBody().close();
    }

    protected void get(String path, HandlerMethod method) {
        addPath("get", path, method);
    }

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
