package core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class BrowserOptionsFactory {

    private static final String[] CHROME_STABILITY_ARGS = {
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--window-size=1920,1080",
            "--remote-allow-origins=*",
            "--disable-extensions",
            "--disable-notifications",
            "--disable-popup-blocking",
            "--disable-infobars",
            "--ignore-certificate-errors",
            "--disable-blink-features=AutomationControlled",
            "--disable-features=AutofillServerCommunication,PasswordLeakDetection,PasswordCheck,SafeBrowsingEnhancedProtection"
    };

    private static final Map<String, Object> CHROME_PRIVACY_PREFS = Map.ofEntries(
            Map.entry("autofill.profile_enabled",                             false),
            Map.entry("autofill.credit_card_enabled",                         false),
            Map.entry("credentials_enable_service",                           false),
            Map.entry("profile.password_manager_enabled",                     false),
            Map.entry("profile.password_manager_leak_detection",              false),
            Map.entry("profile.default_content_setting_values.notifications", 2),
            Map.entry("safebrowsing.enabled",                                 false),
            Map.entry("signin.allowed_on_next_startup",                       false)
    );

    private BrowserOptionsFactory() {}

    public static MutableCapabilities build(String browser, boolean headless) {
        switch (browser == null ? "chrome" : browser.toLowerCase()) {
            case "firefox": return buildFirefox(headless);
            case "chrome":
            default:        return buildChrome(headless);
        }
    }

    private static ChromeOptions buildChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        if (headless) options.addArguments("--headless=new");

        options.addArguments(CHROME_STABILITY_ARGS);
        options.addArguments("--user-data-dir=" + createTempProfile());
        options.setExperimentalOption("prefs", CHROME_PRIVACY_PREFS);

        // 🔑 Suppress “Chrome is being controlled by automated test software” infobar
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        return options;
    }

    private static FirefoxOptions buildFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();

        if (headless) options.addArguments("-headless");
        options.addArguments("--width=1920", "--height=1080");

        Map<String, Object> prefs = Map.of(
                "dom.webnotifications.enabled", false,
                "dom.push.enabled",             false,
                "signon.rememberSignons",       false,
                "browser.formfill.enable",      false
        );
        prefs.forEach(options::addPreference);

        return options;
    }

    private static String createTempProfile() {
        try {
            Path tempProfile = Files.createTempDirectory("chrome-profile-");
            return tempProfile.toAbsolutePath().toString();
        } catch (Exception e) {
            return System.getProperty("java.io.tmpdir");
        }
    }
}