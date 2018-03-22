package ru.otus.sokolovsky.hw15.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

class Parcel {
    static <E> E unpack(String json, Class<E> c) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, c);
    }

    static String pack(Object obj) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(obj);
    }

    @SuppressWarnings("unchecked")
    static Map<String, Object> createMap(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json , Map.class);
    }
}
