package com.cs240.familymap;

import com.cs240.familymapmodules.results.Result;
import com.cs240.familymapmodules.util.Encoder;

import java.net.HttpURLConnection;

public class Response {
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
