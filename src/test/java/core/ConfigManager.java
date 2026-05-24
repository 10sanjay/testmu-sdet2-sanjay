package core;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {
    private static final Properties PROPS = new Properties();

    static {
        String path = "config/config.properties";
        ClassLoader cl = ConfigManager.class.getClassLoader();
        try (InputStream in = cl.getResourceAsStream(path)) {
            if (in == null) {
                throw new RuntimeException(
                    "❌ config.properties NOT FOUND on classpath at '" + path + "'.\n" +
                    "   Expected file location: src/test/resources/config/config.properties\n" +
                    "   FIX: In IntelliJ → right-click 'src/test/resources' → Mark Directory As → Test Resources Root → Rebuild."
                );
            }
            PROPS.load(in);
            System.out.println("✅ Loaded " + PROPS.size() + " properties from " + path);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config: " + e.getMessage(), e);
        }
    }

    private ConfigManager() {}
    public static String  get(String k)     { return System.getProperty(k, PROPS.getProperty(k)); }
    public static boolean getBool(String k) { return Boolean.parseBoolean(get(k)); }
    public static int     getInt(String k)  { return Integer.parseInt(get(k)); }
}