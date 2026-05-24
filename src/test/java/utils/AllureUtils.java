package utils;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;

public final class AllureUtils {
    private AllureUtils() {}
    public static void attachScreenshot(String name, byte[] png) {
        if (png != null && png.length > 0)
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(png), ".png");
    }
    public static void attachJson(String name, String json) {
        Allure.addAttachment(name, "application/json", json);
    }
    public static void attachText(String name, String text) {
        Allure.addAttachment(name, "text/plain", text);
    }
}