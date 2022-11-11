package com.cs240.familymapmodules.util;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class Encoder {
    private static Gson gson;
    static {
        gson = new Gson();
    }

    public static <T> String Encode(T obj) {
        String s = gson.toJson(obj);

        return s;
    }

    public static <T> T Decode(String s, Class<T> type) {
        T obj = gson.fromJson(s, type);

        return obj;
    }

    public static <T> T DecodeFromFilePath(String filePath, Class<T> type) throws FileNotFoundException {
        Reader reader = new FileReader(filePath);

        T obj = gson.fromJson(reader, type);

        return obj;
    }
}
