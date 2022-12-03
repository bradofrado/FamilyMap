package com.cs240.familymap;

import com.cs240.familymapmodules.requests.*;
import com.cs240.familymapmodules.results.*;
import com.cs240.familymapmodules.util.Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
    private String serverHost;
    private String serverPort;
    private String authToken;

    public PersonsResult getPersons(String authToken) {
        setAuthToken(authToken);

        Response response = get("/person");

        return response.parseData(PersonsResult.class);
    }

    public EventsResult getEvents(String authToken) {
        setAuthToken(authToken);

        Response response = get("/event");

        return response.parseData(EventsResult.class);
    }

    public LoginResult login(LoginRequest request) {
        Response response = post("/user/login", Encoder.Encode(request));

        return response.parseData(LoginResult.class);
    }

    public RegisterResult register(RegisterRequest request) {
        Response response = post("/user/register", Encoder.Encode(request));

        return response.parseData(RegisterResult.class);
    }

    public ServerFacade(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    private void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private Response performRequest(String method, String path, String requestBody) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + path);

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod(method);

            http.setDoOutput(requestBody != null);

            http.addRequestProperty("Authorization", getAuthToken());
            http.addRequestProperty("Accept", "application/json");

            http.connect();

            if (requestBody != null) {
                //For post
                OutputStream request = http.getOutputStream();
                writeString(requestBody, request);
                request.close();
            }

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream response = http.getInputStream();

                String responseString = readString(response);

                return new Response(responseString, HttpURLConnection.HTTP_OK);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());

                InputStream response = http.getErrorStream();

                String data = readString(response);

                System.out.println(data);

                return new Response(data, http.getResponseCode());
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new Response(null, HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    private Response get(String path) {
        return performRequest("GET", path, null);
    }

    private Response post(String path, String request) {
        return performRequest("POST", path, request);
    }

    private String getAuthToken() {
        return authToken;
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

    private class Response {
        String body;
        int responseCode;

        public Response(String body, int responseCode) {
            this.body = body;
            this.responseCode = responseCode;
        }

        public <T> T parseData(Class<T> type) {
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Result result = new Result();
                result.setSuccess(false);
                String s = Encoder.Encode(result);
                return Encoder.Decode(s, type);
            }
            return Encoder.Decode(body, type);
        }
    }
}
