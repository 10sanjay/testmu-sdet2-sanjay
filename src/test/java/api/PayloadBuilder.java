package api;

import utils.DynamicDataGenerator; 

import java.util.HashMap;
import java.util.Map;

public final class PayloadBuilder {

    private PayloadBuilder() {}

    public static Map<String, Object> loginPayload(String user, String pass) {
        Map<String, Object> m = new HashMap<>();
        m.put("username", user);
        m.put("password", pass);
        m.put("expiresInMins", 30);
        return m;
    }

    public static Map<String, Object> newUserPayload() {
        Map<String, Object> m = new HashMap<>();
        m.put("firstName", DynamicDataGenerator.firstName());
        m.put("lastName",  DynamicDataGenerator.lastName());
        m.put("age",       DynamicDataGenerator.age());
        m.put("email",     DynamicDataGenerator.email());
        m.put("username",  DynamicDataGenerator.username());
        return m;
    }

    public static Map<String, Object> updateUserPayload() {
        Map<String, Object> m = new HashMap<>();
        m.put("lastName", DynamicDataGenerator.lastName());
        return m;
    }
}