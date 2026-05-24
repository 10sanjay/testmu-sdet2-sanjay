package utils;

import core.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtil {

    private ScreenshotUtil() {}

    public static byte[] capture() {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) return new byte[0];
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.err.println("⚠ Screenshot capture failed: " + e.getMessage());
            return new byte[0];
        }
    }
}