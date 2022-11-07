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

    public static PersonsResult getPersons() {
        ServerFacade facade = new ServerFacade();
        Response response = facade.get("/persons");

        return response.parseData(PersonsResult.class);
    }

    public static EventsResult getEvents() {
        ServerFacade facade = new ServerFacade();
        Response response = facade.get("/events");

        return response.parseData(EventsResult.class);
    }

    public static LoginResult login(LoginRequest request) {
        ServerFacade facade = new ServerFacade();
        Response response = facade.post("/login", Encoder.Encode(request));

        return response.parseData(LoginResult.class);
    }

    public static RegisterResult register(RegisterRequest request) {
        ServerFacade facade = new ServerFacade();
        Response response = facade.post("/register", Encoder.Encode(request));

        return response.parseData(RegisterResult.class);
    }

    private ServerFacade(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    private ServerFacade() {
        this("localhost", "8080");
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


        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private Response get(String path) {
        return performRequest("GET", path, null);
    }

    private Response post(String path, String request) {
        return performRequest("POST", path, request);
    }

    private String getAuthToken() {
        return "abc";
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
