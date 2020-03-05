package com.zhulinyin.multiprocess_eventbus.utils;

import com.google.gson.Gson;

public class SerializeUtil {

    private static final Gson GSON = new Gson();

    public static String encode(Object object) {
        if (object != null) {
            try {
                return GSON.toJson(object);
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> T decode(String data, Class<T> clazz) {
        try {
            return GSON.fromJson(data, clazz);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
