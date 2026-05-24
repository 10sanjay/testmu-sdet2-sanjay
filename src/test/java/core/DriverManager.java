package core;

import java.time.Duration;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Manages WebDriver lifecycle per thread.
 * Browser-specific configuration is delegated to {@link BrowserOptionsFactory}.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {}

    public static void initDriver(String browser) {
        boolean headless = ConfigManager.getBool("browser.headless");
        MutableCapabilities options = BrowserOptionsFactory.build(browser, headless);
        WebDriver driver = createDriver(browser, options);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        if (DRIVER.get() != null) {
            DRIVER.get().quit();
            DRIVER.remove();
        }
    }

    // ───────── private ─────────
    private static WebDriver createDriver(String browser, MutableCapabilities options) {
        switch (browser == null ? "chrome" : browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver((FirefoxOptions) options);

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver((ChromeOptions) options);
        }
    }
}