package com.dys.instantshopping.utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sagi on 29/04/2016.
 */
public class AppCache {
    private static Map<String, Object> data = new HashMap<String, Object>();

    public static void put(String id, Object object) {
        data.put(id, object);
    }

    public static Object get(String id) {
        return data.get(id);
    }
}
