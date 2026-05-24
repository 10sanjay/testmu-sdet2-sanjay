package core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Builds browser-specific options.
 * Keeps DriverManager focused on lifecycle, not configuration details.
 */
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
            "--disable-features=AutofillServerCommunication"
    };

    private static final Map<String, Object> CHROME_PRIVACY_PREFS = Map.of(
            "autofill.profile_enabled",                                  false,
            "autofill.credit_card_enabled",                              false,
            "credentials_enable_service",                                false,
            "profile.password_manager_enabled",                          false,
            "profile.default_content_setting_values.notifications",      2
    );

    private BrowserOptionsFactory() {}

    public static MutableCapabilities build(String browser, boolean headless) {
        switch (browser == null ? "chrome" : browser.toLowerCase()) {
            case "firefox": return buildFirefox(headless);
            case "chrome":
            default:        return buildChrome(headless);
        }
    }

    // ───────── Chrome ─────────
    private static ChromeOptions buildChrome(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        if (headless) options.addArguments("--headless=new");

        options.addArguments(CHROME_STABILITY_ARGS);
        options.addArguments("--user-data-dir=" + createTempProfile());
        options.setExperimentalOption("prefs", CHROME_PRIVACY_PREFS);

        return options;
    }

    // ───────── Firefox ─────────
    private static FirefoxOptions buildFirefox(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();

        if (headless) options.addArguments("-headless");
        options.addArguments("--width=1920", "--height=1080");

        // Stability + privacy
        Map<String, Object> prefs = Map.of(
                "dom.webnotifications.enabled",  false,
                "dom.push.enabled",              false,
                "signon.rememberSignons",        false,
                "browser.formfill.enable",       false
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