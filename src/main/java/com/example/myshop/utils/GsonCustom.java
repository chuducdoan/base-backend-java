package com.example.myshop.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonCustom {

    private static Gson gson;

    private GsonCustom() {
    }

    public static Gson getGsonBuilder() {
        if (gson == null) {
            synchronized (GsonCustom.class) {
                if (null == gson) {
                    gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                }
            }
        }
        return gson;
    }
}
