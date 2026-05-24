package core;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private DriverManager() {}

    public static void initDriver(String browser) {
        boolean headless = ConfigManager.getBool("browser.headless");
        WebDriver d;
        switch (browser == null ? "chrome" : browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fo = new FirefoxOptions();
                if (headless) fo.addArguments("-headless");
                d = new FirefoxDriver(fo); break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions co = new ChromeOptions();
                if (headless) co.addArguments("--headless=new");
                co.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
                d = new ChromeDriver(co);
        }
        d.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        d.manage().window().maximize();
        DRIVER.set(d);
    }

    public static WebDriver getDriver() { return DRIVER.get(); }

    public static void quitDriver() {
        if (DRIVER.get() != null) {
            DRIVER.get().quit();
            DRIVER.remove();
        }
    }
}