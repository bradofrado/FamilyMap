package util;

import com.google.gson.Gson;

public class Encoder {
    public static <T> String Encode(T obj) {
        Gson gson = new Gson();
        String s = gson.toJson(obj);

        return s;
    }

    public static <T> T Decode(String s, Class<T> type) {
        Gson gson = new Gson();
        T obj = gson.fromJson(s, type);

        return obj;
    }
}
