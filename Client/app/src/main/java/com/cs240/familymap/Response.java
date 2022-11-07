package com.cs240.familymap;

import com.cs240.familymapmodules.util.Encoder;

public class Response {
    String body;
    int responseCode;

    public Response(String body, int responseCode) {
        this.body = body;
        this.responseCode = responseCode;
    }

    public <T> T parseData(Class<T> type) {
        return Encoder.Decode(body, type);
    }
}
