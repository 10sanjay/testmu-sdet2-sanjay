package core;

import java.util.HashMap;
import java.util.Map;

public final class ScenarioContext {
    private static final ThreadLocal<Map<String,Object>> CTX = ThreadLocal.withInitial(HashMap::new);
    private ScenarioContext() {}
    public static void set(String k, Object v) { CTX.get().put(k, v); }
    @SuppressWarnings("unchecked")
    public static <T> T get(String k)          { return (T) CTX.get().get(k); }
    public static void clear()                 { CTX.get().clear(); CTX.remove(); }
}