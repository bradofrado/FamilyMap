package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import results.Result;
import util.Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Handler implements HttpHandler {
    private List<Route> routes = new ArrayList<>();
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

            //Create the request object
            Route route = getRoute(method, path);
            if (route == null) {
                sendStatus(HttpURLConnection.HTTP_NOT_FOUND);
            }

            //Get the authorization token
            Headers header=exchange.getRequestHeaders();
            String authToken=header.containsKey("Authorization") ? header.getFirst("Authorization") : null;

            String body = readString(exchange.getRequestBody());
            Request request=new Request(path, authToken, body, route.getParameters(path));

            route.getHandlerMethod().run(request);
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
     *
     */
    protected void sendResult(Result result) throws IOException {
        if (result.isSuccess()) {
            send(HttpURLConnection.HTTP_OK, Encoder.Encode(result));
        } else {
            send(HttpURLConnection.HTTP_INTERNAL_ERROR, Encoder.Encode(result));
        }
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

    private Route getRoute(String method, String path) {
        for (Route route : routes) {
            if (route.checkRoute(method, path)) {
                return route;
            }
        }

        return null;
    }

    private void addPath(String method, String path, HandlerMethod handlerMethod) {
        Route route = new Route(method, path, handlerMethod);

        routes.add(route);
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

    private class Route {
        static final String REGEX = "\\{(\\w*)\\}";

        String path;
        String method;
        List<String> parameters;
        HandlerMethod handlerMethod;

        public Route(String method, String path, HandlerMethod handlerMethod) {
            this.path = path;
            this.method = method;
            this.handlerMethod = handlerMethod;

            parameters = createParameters(path);
        }

        public String getPath() {
            return path;
        }

        public String getMethod() {
            return method;
        }

        public HandlerMethod getHandlerMethod() {
            return handlerMethod;
        }

        public boolean checkRoute(String method, String path) {
            if (!method.equals(this.method)) return false;

            Pattern pattern = Pattern.compile(getPathRegex(this.path));
            Matcher matcher = pattern.matcher(path);

            return matcher.matches();
        }

        public Map<String, String> getParameters(String path) {
            Map<String, String> paramsMap = new HashMap<>();

            String regex = getPathRegex(this.path);//"\\/events\\/(\\w*)";

            if (!checkRoute(this.method, path)) {
                return null;
            }

            // /events/asdf
            // /events/{id}
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(path);
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String val = matcher.group(i);
                paramsMap.put(parameters.get(i-1), val);
            }

            return paramsMap;
        }

        private String getPathRegex(String origPath) {
            return origPath.replaceAll("/", "\\\\/").replaceAll(REGEX, "([\\\\w|\\\\-]*)");
        }

        private List<String> createParameters(String path) {
            List<String> params = new ArrayList<>();

            Pattern pattern = Pattern.compile(REGEX);
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String group = matcher.group(i);
                    params.add(group);
                }
            }

            return params;
        }

    }
}
